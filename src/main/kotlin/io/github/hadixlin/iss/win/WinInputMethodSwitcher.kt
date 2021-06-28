package io.github.hadixlin.iss.win

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import io.github.hadixlin.iss.InputMethodSwitcher

class WinInputMethodSwitcher : InputMethodSwitcher {

    private var lastInputSource: Long = -1

    override fun storeCurrentThenSwitchToEnglish() {
        val hwnd = WinNative.INSTANCE.GetForegroundWindow()
        val current = getCurrentInputSource(hwnd)
        lastInputSource = current
        if (current == KEY_LAYOUT_US) {
            return
        }
        switchToInputSource(hwnd, KEY_LAYOUT_US)
    }

    private fun switchToInputSource(hwnd: WinDef.HWND, inputSourceId: Long) {
        WinNative.INSTANCE.PostMessage(
            hwnd,
            WM_INPUTLANGCHANGEREQUEST,
            WinDef.WPARAM(0),
            WinDef.LPARAM(inputSourceId)
        )
    }

    override fun restore() {
        if (lastInputSource < 0) {
            return
        }
        val hwnd = WinNative.INSTANCE.GetForegroundWindow()
        switchToInputSource(hwnd, lastInputSource)
        lastInputSource = -1
    }

    override fun switchToEnglish() {
        val hwnd = WinNative.INSTANCE.GetForegroundWindow()
        switchToInputSource(hwnd, KEY_LAYOUT_US)
    }

    companion object {
        private const val KEY_LAYOUT_US: Long = 0x4090409
        private const val WM_INPUTLANGCHANGEREQUEST = 0x0050

        private fun getCurrentInputSource(hwnd: WinDef.HWND?): Long {
            val handle = hwnd ?: WinNative.INSTANCE.GetForegroundWindow()
            val pid = WinNative.INSTANCE.GetWindowThreadProcessId(handle, null)
            val hkl = WinNative.INSTANCE.GetKeyboardLayout(WinDef.DWORD(pid.toLong()))
            return Pointer.nativeValue(hkl.pointer)
        }
    }

}
