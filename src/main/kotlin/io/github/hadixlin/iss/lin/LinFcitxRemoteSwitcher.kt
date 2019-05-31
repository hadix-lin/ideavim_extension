package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.concurrent.TimeUnit

class LinFcitxRemoteSwitcher : InputMethodSwitcher {

    override fun switchToEnglish() {
        execFcitxRemote(FCITX_INACTIVE)
    }

    override fun restore() {
        execFcitxRemote(FCITX_ACTIVE)
    }

    private fun execFcitxRemote(cmd: Array<String>): Int {
        val proc = Runtime.getRuntime().exec(cmd)
        proc.waitFor(3, TimeUnit.SECONDS)
        return proc.exitValue()
    }

    companion object {
        private const val FCITX_REMOTE = "fcitx-remote"
        private val FCITX_ACTIVE = arrayOf(FCITX_REMOTE, "-o") //activate input method
        private val FCITX_INACTIVE = arrayOf(FCITX_REMOTE, "-c") //inactivate input method
    }
}