package io.github.hadixlin.iss

import com.maddyhome.idea.vim.ex.vimscript.VimScriptGlobalEnvironment
import io.github.hadixlin.iss.lin.LinFcitxRemoteSwitcher
import io.github.hadixlin.iss.lin.LinuxIbusSwitcher
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
        SystemUtils.IS_OS_LINUX -> {
//            获取输入法设置的环境变量
            val qtInputMethod = System.getenv(QT_INPUT_METHOD)
            val gtkInputMethod = System.getenv(GTK_INPUT_METHOD)
//            当前系统环境变量判断
            if (isFcitx(qtInputMethod, gtkInputMethod)) {
                LinFcitxRemoteSwitcher()
            } else if (qtInputMethod == INPUT_METHOD_IBUS || gtkInputMethod == INPUT_METHOD_IBUS) {
                LinuxIbusSwitcher()
            }else{
                throw IllegalArgumentException("Not Support Current Input Method, Only Support Windows, MacOS and Linux(with fcitx and ibus)")
            }
        }
        else -> throw IllegalArgumentException("Not Support Current System OS, Only Support Windows, MacOS and Linux(with fcitx and ibus)")
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
        private const val INPUT_METHOD_FCITX="fcitx"
        private const val INPUT_METHOD_FCITX5="fcitx5"
        private const val INPUT_METHOD_IBUS="ibus"
        private const val QT_INPUT_METHOD="QT_IM_MODULE"
        private const val GTK_INPUT_METHOD="GTK_IM_MODULE"

        fun isFcitx(qtInputMethod :String,gtkInputMethod :String) :Boolean {
            return qtInputMethod == INPUT_METHOD_FCITX || gtkInputMethod == INPUT_METHOD_FCITX||qtInputMethod== INPUT_METHOD_FCITX5||gtkInputMethod== INPUT_METHOD_FCITX5
        }
    }
}
