package io.github.hadixlin.iss.win

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import io.github.hadixlin.iss.InputSourceSwitcher

class WinInputSourceSwitcher : InputSourceSwitcher {

    private var lastInputSource: Long = -1

    override fun switchToEnglish() {
        val hwnd = WinNative.INSTANCE.GetForegroundWindow()
        val current = getCurrentInputSource(hwnd)
        lastInputSource = current
        if (current == KEY_LAYOUT_US) {
            return
        }
        switchToInputSource(hwnd, KEY_LAYOUT_US)
    }

    private fun switchToInputSource(hwnd: WinDef.HWND, inputSourceId: Long) {
        WinNative.INSTANCE.PostMessage(hwnd, 0x0050, WinDef.WPARAM(0), WinDef.LPARAM(inputSourceId))
    }

    override fun restore() {
        val hwnd = WinNative.INSTANCE.GetForegroundWindow()
        val current = getCurrentInputSource(hwnd)
        if (lastInputSource < 0 || lastInputSource == current) {
            return
        }
        switchToInputSource(hwnd, lastInputSource)
    }

    companion object {
        private const val KEY_LAYOUT_US: Long = 0x4090409

        private fun getCurrentInputSource(hwnd: WinDef.HWND): Long {
            val pid = WinNative.INSTANCE.GetWindowThreadProcessId(hwnd, null)
            val hkl = WinNative.INSTANCE.GetKeyboardLayout(WinDef.DWORD(pid.toLong()))
            return Pointer.nativeValue(hkl.pointer)
        }
    }

}
