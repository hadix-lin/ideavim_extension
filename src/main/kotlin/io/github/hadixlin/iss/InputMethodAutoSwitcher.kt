package io.github.hadixlin.iss

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.util.messages.MessageBusConnection
import com.maddyhome.idea.vim.command.CommandState
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.extension.VimExtensionFacade
import com.maddyhome.idea.vim.helper.StringHelper
import com.maddyhome.idea.vim.key.MappingOwner
import org.apache.commons.lang.StringUtils
import java.lang.Long.MAX_VALUE
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object InputMethodAutoSwitcher {
    private const val VIM_INSERT_EXIT_MODE_ACTION = "VimInsertExitModeAction"

    private val EDITING_MODE = EnumSet.of(
        CommandState.Mode.INSERT,
        CommandState.Mode.REPLACE
    )

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
            val commandName = commandEvent.commandName
            if (StringUtils.isBlank(commandName)) {
                return
            }
            if (commandName == VIM_INSERT_EXIT_MODE_ACTION) {
                executor?.execute { switcher.storeCurrentThenSwitchToEnglish() }
                return
            }
        }

        override fun commandFinished(event: CommandEvent) {
            if (!restoreInInsert) {
                return
            }
            val currentEditor = currentEditor(event) ?: return
            val state = CommandState.getInstance(currentEditor)
            if (state.mode in EDITING_MODE) {
                executor?.execute { switcher.restore() }
            }
        }

        private fun currentEditor(commandEvent: CommandEvent): Editor? {
            val cmdGroupId = commandEvent.commandGroupId
            return try {
                val editorField = cmdGroupId?.javaClass?.getDeclaredField("editor")
                editorField?.isAccessible = true
                editorField?.get(cmdGroupId) as Editor?
            } catch (e: NoSuchFieldException) {
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
            MappingOwner.Plugin.get("IdeaVimExtension"),
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
        eventMulticaster.addFocusChangeListener(focusListener, Disposable {})
    }

    private val focusListener = object : FocusChangeListener {

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


    fun disable() {
        if (!enabled) {
            return
        }
        messageBusConnection?.disconnect()
        executor?.shutdown()
        enabled = false
    }
}