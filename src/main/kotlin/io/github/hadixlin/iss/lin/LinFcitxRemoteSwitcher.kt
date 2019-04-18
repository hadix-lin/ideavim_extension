package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputSourceSwitcher
import java.util.concurrent.TimeUnit

class LinFcitxRemoteSwitcher : InputSourceSwitcher {

    override fun switchToEnglish() {
        execFcitxRemote(FCTIX_INACTIVE)
    }

    override fun restore() {
        execFcitxRemote(FCTIX_ACTIVE)
    }

    private fun execFcitxRemote(cmd: Array<String>): Int {
        val proc = Runtime.getRuntime().exec(cmd)
        proc.waitFor(3, TimeUnit.SECONDS)
        return proc.exitValue()
    }

    companion object {
        private const val FCITX_REMOTE = "fcitx-remote"
        private val FCTIX_ACTIVE = arrayOf(FCITX_REMOTE, "-o") //activate input method
        private val FCTIX_INACTIVE = arrayOf(FCITX_REMOTE, "-c") //inactivate input method
    }
}