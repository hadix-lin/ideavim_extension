package io.github.hadixlin.iss

import io.github.hadixlin.iss.lin.LinFcitxRemoteSwitcher
import io.github.hadixlin.iss.mac.MacInputSourceSwitcher
import io.github.hadixlin.iss.win.WinInputSourceSwitcher
import org.apache.commons.lang.SystemUtils

/** Created by hadix on 26/12/2018.  */
class SystemInputSourceSwitcher : InputSourceSwitcher {
	private var delegate: InputSourceSwitcher = when {
		SystemUtils.IS_OS_WINDOWS -> WinInputSourceSwitcher()
		SystemUtils.IS_OS_MAC -> MacInputSourceSwitcher()
		SystemUtils.IS_OS_LINUX -> LinFcitxRemoteSwitcher()
		else -> throw IllegalArgumentException("Not Support Current System OS, Only Support Windows And MacOS")
	}

	override fun switchToEnglish() {
		delegate.switchToEnglish()
	}

	override fun restore() {
		delegate.restore()
	}
}
