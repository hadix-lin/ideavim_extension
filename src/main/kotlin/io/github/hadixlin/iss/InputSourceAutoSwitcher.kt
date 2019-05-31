package io.github.hadixlin.iss

import com.intellij.openapi.Disposable
import com.intellij.openapi.command.CommandAdapter
import com.intellij.openapi.command.CommandEvent
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

object InputSourceAutoSwitcher {

    @Volatile
    var restoreInInsert: Boolean = false

    private var executor: ThreadPoolExecutor? = null

    private val switcher = SystemInputSourceSwitcher()

    private val exitInsertModeListener = object : CommandAdapter() {

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
                    commandName = "Vim " + vimCmd.action.templatePresentation.text
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
        CommandProcessor.getInstance().removeCommandListener(exitInsertModeListener)
        unregisterFocusChangeListener()
        executor?.shutdown()
    }

    private val focusListener = object : FocusChangeListener {
        private val EDITING_MODE = EnumSet.of(
            CommandState.Mode.INSERT,
            CommandState.Mode.REPLACE
        )

        override fun focusLost(editor: Editor?) {}

        override fun focusGained(editor: Editor?) {
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

        val methods = EditorEventMulticasterEx::class.java.methods
        //IDEA-2018.3以前方法名称为addFocusChangeListner,2018.3后修正为addFocusChangeListener
        val func = methods.find {
            it.name in setOf(
                "addFocusChangeListner",
                "addFocusChangeListener"
            ) && it.parameterCount == 2
        } ?: throw IllegalArgumentException("找不到addFocusChangeListener或addFocusChangeListner")
        func.invoke(eventMulticaster, focusListener, Disposable {})
    }

    private fun unregisterFocusChangeListener() {
        val eventMulticaster =
            EditorFactory.getInstance().eventMulticaster as? EditorEventMulticasterEx ?: return
        val methods = EditorEventMulticasterEx::class.java.methods
        //IDEA-2018.3以前方法名称为removeFocusChangeListner,2018.3后修正为removeFocusChangeListener
        val func = methods.find {
            it.name in setOf(
                "removeFocusChangeListner",
                "removeFocusChangeListener"
            ) && it.parameterCount == 1
        } ?: throw IllegalArgumentException("找不到removeFocusChangeListener或removeFocusChangeListner")
        func.invoke(eventMulticaster, focusListener)
    }

    private val SWITCH_TO_ENGLISH_COMMAND_NAMES = setOf("Vim Exit Insert Mode")

    private val SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES = setOf(
        "Vim Insert After Cursor",
        "Vim Insert After Line End",
        "Vim Insert Before Cursor",
        "Vim Insert Before First non-Blank",
        "Vim Insert Character Above Cursor",
        "Vim Insert Character Below Cursor",
        "Vim Delete Inserted Text",
        "Vim Delete Previous Word",
        "Vim Enter",
        "Vim Insert at Line Start",
        "Vim Insert New Line Above",
        "Vim Insert New Line Below",
        "Vim Insert Previous Text",
        "Vim Insert Previous Text",
        "Vim Insert Register",
        "Vim Toggle Insert/Replace",
        "Vim Change Line",
        "Vim Change Character",
        "Vim Change Characters",
        "Vim Replace"
    )
}