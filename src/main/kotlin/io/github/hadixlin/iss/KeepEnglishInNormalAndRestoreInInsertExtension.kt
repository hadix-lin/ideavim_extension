package io.github.hadixlin.iss

import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.extension.VimExtension
import com.maddyhome.idea.vim.vimscript.services.OptionService

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
        VimPlugin.getOptionService().setOption(OptionService.Scope.GLOBAL, KeepEnglishInNormalExtension.NAME)
    }

    override fun dispose() {
        InputMethodAutoSwitcher.restoreInInsert = false
    }

    companion object {
        private const val NAME = "keep-english-in-normal-and-restore-in-insert"
    }
}

