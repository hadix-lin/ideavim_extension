package io.github.hadixlin.iss

import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.editor.ex.FocusChangeListener
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
                    commandName = "Vim " + vimCmd.action.id
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
        CommandProcessor.getInstance().addCommandListener(exitInsertModeListener)
        registerFocusChangeListener()
        VimExtensionFacade.putKeyMapping(
            MappingMode.N,
            StringHelper.parseKeys("<Esc>"),
            StringHelper.parseKeys("a<Esc><Esc>"),
            false
        )
    }

    fun disable() {
        if (!enabled) {
            return
        }
        CommandProcessor.getInstance().removeCommandListener(exitInsertModeListener)
        executor?.shutdown()
        enabled = false
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

    /**
     * 由于EditorEventMulticasterEx中添加焦点变化监听器的方法在IDEA-2018.3以前有个拼写错误.
     * 所以在此使用反射方法进行调用以向前兼容
     */
    private fun registerFocusChangeListener() {
        val eventMulticaster =
            EditorFactory.getInstance().eventMulticaster as? EditorEventMulticasterEx ?: return
        eventMulticaster.addFocusChangeListener(focusListener) {}
    }

    private val SWITCH_TO_ENGLISH_COMMAND_NAMES = setOf("VimExitInsertMode", "ExitInsertMode")

    private val SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES = setOf(
        "VimInsertAfterCursor",
        "VimInsertAfterCursor",
        "VimInsertAfterLineEnd",
        "VimInsertBeforeCursor",
        "VimInsertBeforeFirstNon-Blank",
        "VimInsertCharacterAboveCursor",
        "VimInsertCharacterBelowCursor",
        "VimDeleteInsertedText",
        "VimDeletePreviousWord",
        "VimEnter",
        "VimInsertLineStart",
        "VimInsertNewLineAbove",
        "VimInsertNewLineBelow",
        "VimInsertPreviousText",
        "VimInsertPreviousText",
        "VimInsertRegister",
        "VimToggleInsert/Replace",
        "VimChangeLine",
        "VimChangeCharacter",
        "VimChangeCharacters",
        "VimReplace",
        "InsertAfterCursor",
        "InsertAfterCursor",
        "InsertAfterLineEnd",
        "InsertBeforeCursor",
        "InsertBeforeFirstNonBlank",
        "InsertCharacterAboveCursor",
        "InsertCharacterBelowCursor",
        "DeleteInsertedText",
        "DeletePreviousWord",
        "Enter",
        "InsertLineStart",
        "InsertNewLineAbove",
        "InsertNewLineBelow",
        "InsertPreviousText",
        "InsertPreviousText",
        "InsertRegister",
        "ToggleInsert/Replace",
        "ChangeLine",
        "ChangeCharacter",
        "ChangeCharacters",
        "Replace"
    )
}