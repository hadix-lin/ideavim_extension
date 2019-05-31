package io.github.hadixlin.iss

import com.maddyhome.idea.vim.extension.VimExtension

/**
 * @author hadix
 * @date 31/03/2017
 */
class KeepEnglishInNormalAndRestoreInInsertExtension : VimExtension {

    override fun getName(): String {
        return "keep-english-in-normal-and-restore-in-insert"
    }

    override fun init() {
        InputSourceAutoSwitcher.restoreInInsert = true
        InputSourceAutoSwitcher.enable()
    }

    override fun dispose() {
        InputSourceAutoSwitcher.disable()
    }

}

