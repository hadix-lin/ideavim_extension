package io.github.hadixlin.iss

import com.maddyhome.idea.vim.extension.VimExtension
import com.maddyhome.idea.vim.option.OptionsManager
import com.maddyhome.idea.vim.option.ToggleOption

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
        val option =
            OptionsManager.getOption(KeepEnglishInNormalExtension.NAME) as ToggleOption?
        option?.set()
    }

    override fun dispose() {
        InputMethodAutoSwitcher.restoreInInsert = false
    }

    companion object {
        const val NAME = "keep-english-in-normal-and-restore-in-insert"
    }
}

