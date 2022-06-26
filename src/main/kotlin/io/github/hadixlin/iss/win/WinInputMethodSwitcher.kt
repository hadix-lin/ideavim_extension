package io.github.hadixlin.iss.win

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import io.github.hadixlin.iss.InputMethodSwitcher

class WinInputMethodSwitcher(
	english: Long?, private var nonEnglish: Long?
) : InputMethodSwitcher {

	private val english: Long = english ?: KEY_LAYOUT_US_DEFAULT
	override fun storeCurrentThenSwitchToEnglish() {
		val hwnd = WinNative.INSTANCE.GetForegroundWindow() ?: return
		val current = getCurrentInputSource(hwnd)
		if (current == english) {
			return
		}
		if (nonEnglish == null) {
			nonEnglish = current
		}
		switchToInputSource(hwnd, english)
	}

	private fun switchToInputSource(hwnd: WinDef.HWND, inputSourceId: Long) {
		WinNative.INSTANCE.PostMessage(
			hwnd,
			WM_INPUT_LANG_CHANGE_REQUEST,
			WinDef.WPARAM(0),
			WinDef.LPARAM(inputSourceId)
		)
	}

	override fun restore() {
		val nonEnglish = this.nonEnglish ?: return
		val hwnd = WinNative.INSTANCE.GetForegroundWindow() ?: return
		switchToInputSource(hwnd, nonEnglish)
	}

	override fun switchToEnglish() {
		val hwnd = WinNative.INSTANCE.GetForegroundWindow() ?: return
		switchToInputSource(hwnd, english)
	}

	companion object {
		private const val KEY_LAYOUT_US_DEFAULT: Long = 0x4090409
		private const val WM_INPUT_LANG_CHANGE_REQUEST = 0x0050

		private fun getCurrentInputSource(hwnd: WinDef.HWND?): Long {
			val handle = hwnd ?: WinNative.INSTANCE.GetForegroundWindow()
			val pid = WinNative.INSTANCE.GetWindowThreadProcessId(handle, null)
			val hkl = WinNative.INSTANCE.GetKeyboardLayout(WinDef.DWORD(pid.toLong()))
			return Pointer.nativeValue(hkl.pointer)
		}
	}

}