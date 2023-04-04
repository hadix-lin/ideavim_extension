package io.github.hadixlin.iss

import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.extension.VimExtension
import com.maddyhome.idea.vim.vimscript.services.OptionService


/**
 * 输入中文
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
        InputMethodAutoSwitcher.focusAuto =
            VimPlugin.getVariableService().getGlobalVariableValue(FOCUS_AUTO)?.asBoolean() ?: true
        VimPlugin.getOptionService().setOption(OptionService.Scope.GLOBAL, KeepEnglishInNormalExtension.NAME)
    }

    override fun dispose() {
        InputMethodAutoSwitcher.restoreInInsert = false
    }

    companion object {
        private const val CONTEXT_WARE = "context_aware"
        private const val FOCUS_AUTO = "focus_auto"
        private const val NAME = "keep-english-in-normal-and-restore-in-insert"
    }
}