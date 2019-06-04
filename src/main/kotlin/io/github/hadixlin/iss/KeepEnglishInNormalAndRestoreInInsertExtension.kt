package io.github.hadixlin.iss

import com.maddyhome.idea.vim.extension.VimExtension

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
        if (!InputMethodAutoSwitcher.enabled) {
            InputMethodAutoSwitcher.enable()
        }
    }

    override fun dispose() {
        InputMethodAutoSwitcher.restoreInInsert = false
    }

    companion object {
        const val NAME = "keep-english-in-normal-and-restore-in-insert"
    }
}

