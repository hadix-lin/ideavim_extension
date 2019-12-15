package io.github.hadixlin.iss

import io.github.hadixlin.iss.lin.LinFcitxRemoteSwitcher
import io.github.hadixlin.iss.mac.MacInputMethodSwitcher
import io.github.hadixlin.iss.win.WinInputMethodSwitcher
import org.apache.commons.lang.SystemUtils

/** Created by hadix on 26/12/2018.  */
class SystemInputMethodSwitcher : InputMethodSwitcher {
    private var delegate: InputMethodSwitcher = when {
        SystemUtils.IS_OS_WINDOWS -> WinInputMethodSwitcher()
        SystemUtils.IS_OS_MAC -> MacInputMethodSwitcher()
        SystemUtils.IS_OS_LINUX -> LinFcitxRemoteSwitcher()
        else -> throw IllegalArgumentException("Not Support Current System OS, Only Support Windows, MacOS and Linux(with fcitx)")
    }

    override fun switchToEnglish() {
        delegate.switchToEnglish()
    }

    override fun restore() {
        delegate.restore()
    }
}
