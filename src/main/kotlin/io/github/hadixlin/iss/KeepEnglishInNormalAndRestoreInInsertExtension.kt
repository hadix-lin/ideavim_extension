package io.github.hadixlin.iss

import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.api.setToggleOption
import com.maddyhome.idea.vim.ex.exExceptionMessage
import com.maddyhome.idea.vim.extension.VimExtension
import com.maddyhome.idea.vim.options.OptionAccessScope
import com.maddyhome.idea.vim.options.ToggleOption


/**
 * @author hadix
 * @date 31/03/2017
 */
class KeepEnglishInNormalAndRestoreInInsertExtension : VimExtension {

    override fun getName(): String {
        return NAME
    }

    override fun init() {
        InputMethodAutoSwitcher.restoreInInsert = true
        InputMethodAutoSwitcher.contextAware =
            VimPlugin.getVariableService().getGlobalVariableValue(CONTEXT_WARE)?.asBoolean() ?: true
        val optionGroup = VimPlugin.getOptionGroup()
        val option =
            (optionGroup.getOption(KeepEnglishInNormalExtension.NAME)
                ?: throw exExceptionMessage("option not found")) as ToggleOption
        optionGroup.setToggleOption(option, OptionAccessScope.GLOBAL(null))
    }

    override fun dispose() {
        InputMethodAutoSwitcher.restoreInInsert = false
    }

    companion object {
        private const val CONTEXT_WARE = "context_aware"
        private const val NAME = "keep-english-in-normal-and-restore-in-insert"
    }
}