package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.*
import java.util.concurrent.TimeUnit

class LinFcitxRemoteSwitcher : InputMethodSwitcher {
    private var lastStatus: Int = 0

    override fun switchToEnglish() {
        val current = getFcitxStatus()
        if (current != STATUS_UNKNOWN) {
            lastStatus = current
        }
        if (current == STATUS_INACTIVE) {
            return
        }
        execFcitxRemote(FCITX_INACTIVE)
    }

    override fun restore() {
        if (lastStatus == STATUS_ACTIVE) {
            execFcitxRemote(FCITX_ACTIVE)
        }
    }

    private fun execFcitxRemote(cmd: Array<String>): Process {
        val proc = Runtime.getRuntime().exec(cmd)
        proc.waitFor(3, TimeUnit.SECONDS)
        return proc
    }

    private fun getFcitxStatus(): Int {
        val proc = execFcitxRemote(FCITX_STATUS)
        return Scanner(proc.inputStream).use {
            if (it.hasNextInt()) {
                it.nextInt()
            } else {
                STATUS_UNKNOWN
            }
        }
    }

    companion object {
        private const val FCITX_REMOTE = "fcitx-remote"
        private const val STATUS_UNKNOWN = -1
        private const val STATUS_INACTIVE = 1;
        private const val STATUS_ACTIVE = 2 // 0 OFF,1 INACTIVE,2 ACTIVE

        private val FCITX_ACTIVE = arrayOf(FCITX_REMOTE, "-o") //activate input method
        private val FCITX_INACTIVE = arrayOf(FCITX_REMOTE, "-c") //inactivate input method
        private val FCITX_STATUS = arrayOf(FCITX_REMOTE) //get fcitx status
    }
}