package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.*
import java.util.concurrent.TimeUnit

class LinFcitxRemoteSwitcher : InputMethodSwitcher {

	override fun storeCurrentThenSwitchToEnglish() {
		switchToEnglish()
	}

	override fun restore() {
		execFcitxRemote(FCITX_ACTIVE)
	}

	override fun switchToEnglish() {
		execFcitxRemote(FCITX_INACTIVE)
	}

	companion object {
		private const val STATUS_UNKNOWN = -1

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

		@Suppress("unused")
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