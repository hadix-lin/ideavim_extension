package io.github.hadixlin.iss

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.util.messages.MessageBusConnection
import com.maddyhome.idea.vim.command.Command
import com.maddyhome.idea.vim.command.CommandState
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.extension.VimExtensionFacade
import com.maddyhome.idea.vim.helper.RunnableHelper
import com.maddyhome.idea.vim.helper.StringHelper
import org.apache.commons.lang.StringUtils
import java.lang.Long.MAX_VALUE
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object InputMethodAutoSwitcher {

    @Volatile
    var restoreInInsert: Boolean = false
    @Volatile
    var enabled: Boolean = false
        private set

    private var executor: ThreadPoolExecutor? = null

    private val switcher = SystemInputMethodSwitcher()

    private var messageBusConnection: MessageBusConnection? = null

    private val exitInsertModeListener = object : CommandListener {

        override fun beforeCommandFinished(commandEvent: CommandEvent) {
            var commandName = commandEvent.commandName
            if (StringUtils.isBlank(commandName)) {
                return
            }
            if (SWITCH_TO_ENGLISH_COMMAND_NAMES.contains(commandName)) {
                executor?.execute { switcher.switchToEnglish() }
                return
            }
            if (!restoreInInsert) {
                return
            }
            if ("Typing" == commandName) {
                val vimCmd = readVimCmd(commandEvent)
                if (vimCmd != null) {
                    commandName = vimCmd.action.id
                } else {
                    return
                }
            }
            if (SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES.contains(commandName)) {
                executor?.execute { switcher.restore() }
            }
        }

        private fun readVimCmd(commandEvent: CommandEvent): Command? {
            val cmd = commandEvent.command
            return if (cmd.javaClass.enclosingClass == RunnableHelper::class.java) {
                val writeActionCmd = cmd.javaClass.getDeclaredField("cmd")
                writeActionCmd.isAccessible = true
                val other = writeActionCmd.get(cmd)
                val actionRunnerCmd = other.javaClass.getDeclaredField("cmd")
                actionRunnerCmd.isAccessible = true
                actionRunnerCmd.get(other) as Command
            } else {
                null
            }
        }
    }

    fun enable() {
        if (enabled) {
            return
        }
        enabled = true
        if (executor?.isShutdown != false) {
            executor = ThreadPoolExecutor(
                1, 1,
                MAX_VALUE, TimeUnit.DAYS,
                ArrayBlockingQueue(10),
                ThreadFactory { r ->
                    val thread = Thread(r, "ideavim_extension")
                    thread.isDaemon = true
                    thread.priority = Thread.MAX_PRIORITY
                    thread
                },
                ThreadPoolExecutor.DiscardPolicy()
            )
        }
        registerExitInsertModeListener()
        registerFocusChangeListener()
        VimExtensionFacade.putKeyMapping(
            MappingMode.N,
            StringHelper.parseKeys("<Esc>"),
            StringHelper.parseKeys("a<Esc><Esc>"),
            false
        )
    }

    private fun registerExitInsertModeListener() {
        messageBusConnection = ApplicationManager.getApplication().messageBus.connect()
        messageBusConnection?.subscribe(CommandListener.TOPIC, exitInsertModeListener)
    }

    private fun registerFocusChangeListener() {
        val eventMulticaster =
            EditorFactory.getInstance().eventMulticaster as? EditorEventMulticasterEx ?: return
        eventMulticaster.addFocusChangeListener(focusListener) {}
    }

    private val focusListener = object : FocusChangeListener {
        private val EDITING_MODE = EnumSet.of(
            CommandState.Mode.INSERT,
            CommandState.Mode.REPLACE
        )

        override fun focusLost(editor: Editor) {}

        override fun focusGained(editor: Editor) {
            if (!enabled) {
                return
            }
            val state = CommandState.getInstance(editor)
            if (state.mode !in EDITING_MODE) {
                executor?.execute { switcher.switchToEnglish() }
            }
        }
    }

    private val SWITCH_TO_ENGLISH_COMMAND_NAMES = setOf("VimInsertExitModeAction")

    private val SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES = setOf(
        "VimInsertAfterCursorAction",
        "VimInsertAfterCursorAction",
        "VimInsertAfterLineEndAction",
        "VimInsertBeforeCursorAction",
        "VimInsertBeforeFirstNonBlankAction",
        "VimInsertCharacterAboveCursorAction",
        "VimInsertCharacterBelowCursorAction",
        "VimInsertDeleteInsertedTextAction",
        "VimInsertDeletePreviousWordAction",
        "VimInsertEnterAction",
        "VimInsertLineStartAction",
        "VimInsertNewLineAboveAction",
        "VimInsertNewLineBelowAction",
        "VimInsertPreviousTextAction",
        "VimInsertPreviousTextAction",
        "VimInsertRegisterAction",
        "VimChangeLineAction",
        "VimChangeCharacterAction",
        "VimChangeCharactersAction",
        "VimChangeReplaceAction"
    )

    fun disable() {
        if (!enabled) {
            return
        }
        messageBusConnection?.disconnect()
        executor?.shutdown()
        enabled = false
    }
}