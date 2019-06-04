package io.github.hadixlin.iss

import com.maddyhome.idea.vim.extension.VimExtension

/**
 * @author hadix
 * @date 09/04/2017
 */
class KeepEnglishInNormalExtension : VimExtension {

    override fun getName(): String {
        return NAME
    }

    override fun init() {
        InputMethodAutoSwitcher.enable()
    }


    override fun dispose() {
        InputMethodAutoSwitcher.disable()
    }

    companion object {
        const val NAME = "keep-english-in-normal"
    }

}
