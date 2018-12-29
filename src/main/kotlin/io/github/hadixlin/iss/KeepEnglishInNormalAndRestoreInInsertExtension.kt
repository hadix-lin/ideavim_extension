package io.github.hadixlin.iss

import com.intellij.openapi.command.CommandAdapter
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandProcessor
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.extension.VimExtension
import com.maddyhome.idea.vim.extension.VimExtensionFacade
import com.maddyhome.idea.vim.helper.StringHelper.parseKeys
import org.apache.commons.lang.StringUtils
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author hadix
 * @date 31/03/2017
 */
open class KeepEnglishInNormalAndRestoreInInsertExtension(val restoreInInsert: Boolean = true) : VimExtension {

    private val executor = ThreadPoolExecutor(1, 1,
            java.lang.Long.MAX_VALUE, TimeUnit.DAYS,
            ArrayBlockingQueue(10),
            ThreadFactory { r ->
                val thread = Thread(r, "ideavim_extension")
                thread.isDaemon = true
                thread.priority = Thread.MAX_PRIORITY
                thread
            },
            ThreadPoolExecutor.DiscardPolicy())

    private val exitInsertModeListener = object : CommandAdapter() {

        private val switcher = SystemInputSourceSwitcher()

        override fun beforeCommandFinished(commandEvent: CommandEvent) {
            val commandName = commandEvent.commandName
            if (StringUtils.isBlank(commandName)) {
                return
            }
            if (SWITCH_TO_ENGLISH_COMMAND_NAMES.contains(commandName)) {
                executor.execute { switcher.switchToEnglish() }
                return
            }
            if (!restoreInInsert) {
                return
            }
            if (SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES.contains(commandName)) {
                executor.execute { switcher.restore() }
            }
        }
    }

    override fun getName(): String {
        return "keep-english-in-normal-and-restore-in-insert"
    }

    override fun init() {
        CommandProcessor.getInstance().addCommandListener(this.exitInsertModeListener)
        VimExtensionFacade.putKeyMapping(
                MappingMode.N, parseKeys("<Esc>"), parseKeys("a<Esc><Esc>"), false)
    }

    override fun dispose() {
        CommandProcessor.getInstance().removeCommandListener(exitInsertModeListener)
    }

    companion object {

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
                "Vim Replace")
    }
}
