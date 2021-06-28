package io.github.hadixlin.iss

import com.maddyhome.idea.vim.ex.vimscript.VimScriptGlobalEnvironment
import io.github.hadixlin.iss.lin.LinFcitxRemoteSwitcher
import io.github.hadixlin.iss.mac.MacInputMethodSwitcher
import io.github.hadixlin.iss.win.WinInputMethodSwitcher
import org.apache.commons.lang.SystemUtils

/** Created by hadix on 26/12/2018.  */
class SystemInputMethodSwitcher : InputMethodSwitcher {
    private var delegate: InputMethodSwitcher = when {
        SystemUtils.IS_OS_WINDOWS -> WinInputMethodSwitcher()
        SystemUtils.IS_OS_MAC -> {
            val env = VimScriptGlobalEnvironment.getInstance()
            val englishInputSource = env.variables[ENGLISH_INPUT_SOURCE_FOR_MAC]?.toString()
            if (englishInputSource != null) {
                MacInputMethodSwitcher(englishInputSource)
            } else {
                MacInputMethodSwitcher()
            }
        }
        SystemUtils.IS_OS_LINUX -> LinFcitxRemoteSwitcher()
        else -> throw IllegalArgumentException("Not Support Current System OS, Only Support Windows, MacOS and Linux(with fcitx)")
    }

    override fun switchToEnglish() {
        delegate.switchToEnglish()
    }

    override fun storeCurrentThenSwitchToEnglish() {
        delegate.storeCurrentThenSwitchToEnglish()
    }

    override fun restore() {
        delegate.restore()
    }

    companion object {
        private const val ENGLISH_INPUT_SOURCE_FOR_MAC = "keep_input_source_in_normal"
    }
}
