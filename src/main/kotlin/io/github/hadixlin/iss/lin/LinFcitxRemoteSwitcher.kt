package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.*
import java.util.concurrent.TimeUnit

class LinFcitxRemoteSwitcher : InputMethodSwitcher {
    private var lastStatus: Int = STATUS_UNKNOWN

    override fun storeCurrentThenSwitchToEnglish() {
        val current = getFcitxStatus()
        if (current != STATUS_UNKNOWN) {
            lastStatus = current
        }
        if (current == STATUS_INACTIVE) {
            return
        }
        switchToEnglish()
    }

    override fun restore() {
        if (lastStatus == STATUS_ACTIVE) {
            execFcitxRemote(FCITX_ACTIVE)
            lastStatus = STATUS_UNKNOWN
        }
    }

    override fun switchToEnglish() {
        execFcitxRemote(FCITX_INACTIVE)
    }

    companion object {
        private const val STATUS_UNKNOWN = -1
        private const val STATUS_INACTIVE = 1
        private const val STATUS_ACTIVE = 2 // 0 OFF,1 INACTIVE,2 ACTIVE

        private val FCITX_ACTIVE: Array<String>
        private val FCITX_INACTIVE: Array<String>
        private val FCITX_STATUS: Array<String>

        init {
            var fcitxRemote: String
            try {
                fcitxRemote = "fcitx-remote"
                execFcitxRemote(arrayOf(fcitxRemote))
            } catch (e: Exception) {
                fcitxRemote = "fcitx5-remote"
                execFcitxRemote(arrayOf(fcitxRemote))
            }

            FCITX_ACTIVE = arrayOf(fcitxRemote, "-o") // active input method
            FCITX_INACTIVE = arrayOf(fcitxRemote, "-c") //inactivate input method
            FCITX_STATUS = arrayOf(fcitxRemote) //get fcitx status
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
    }
}