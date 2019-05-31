package io.github.hadixlin.iss

import com.maddyhome.idea.vim.extension.VimExtension

/**
 * @author hadix
 * @date 09/04/2017
 */
class KeepEnglishInNormalExtension : VimExtension {

    override fun getName(): String {
        return "keep-english-in-normal"
    }

    override fun init() {
        InputSourceAutoSwitcher.restoreInInsert = false
        InputSourceAutoSwitcher.enable()
    }


    override fun dispose() {
        InputSourceAutoSwitcher.disable()
    }

}
