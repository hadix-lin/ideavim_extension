package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.*
import java.util.concurrent.TimeUnit

class LinFcitx5RimeSwitcher : InputMethodSwitcher {

    private var lastStatus = STATUS_ASCII

	override fun storeCurrentThenSwitchToEnglish() {
        val currentStatus = getFcitxRimeStatus()
        lastStatus = currentStatus
		if (currentStatus == STATUS_IME){
			switchToEnglish()
		}
	}

	override fun restore() {
        if (lastStatus != STATUS_ASCII){
            execFcitxRime(FCITX_RIME_IME)
        }
	}

	override fun switchToEnglish() {
		execFcitxRime(FCITX_RIME_ASCII)
	}

	companion object {
		private const val BUS_TYPE = "b"
		private const val STATUS_ASCII = "true"
		private const val STATUS_IME = "false"

		private val FCITX_RIME_IME: Array<String>
		private val FCITX_RIME_ASCII: Array<String>
		private val FCITX_RIME_STATUS: Array<String>

		init {
			val busctlCall: Array<String>

			busctlCall = arrayOf("busctl",
             "call", "--user", "org.fcitx.Fcitx5",
             "/rime", "org.fcitx.Fcitx.Rime1")

			FCITX_RIME_IME = busctlCall.plus(arrayOf("SetAsciiMode", BUS_TYPE, STATUS_IME)) // active input method
			FCITX_RIME_ASCII = busctlCall.plus(arrayOf("SetAsciiMode", BUS_TYPE, STATUS_ASCII)) //inactivate input method
			FCITX_RIME_STATUS = busctlCall.plus(arrayOf("IsAsciiMode")) //get fcitx status
		}

		private fun execFcitxRime(cmd: Array<String>): Process {
			val proc = Runtime.getRuntime().exec(cmd)
			proc.waitFor(3, TimeUnit.SECONDS)
			return proc
		}

		private fun getFcitxRimeStatus(): String {
			val proc = execFcitxRime(FCITX_RIME_STATUS)
			return Scanner(proc.inputStream).use {
				if (it.hasNext()) {
					val firstToken = it.next()
                    if (firstToken == BUS_TYPE && it.hasNextBoolean()){
                        "${it.nextBoolean()}"
                    }else {
                        STATUS_ASCII
                    }
				} else {
					STATUS_ASCII
				}
			}
		}
	}

}