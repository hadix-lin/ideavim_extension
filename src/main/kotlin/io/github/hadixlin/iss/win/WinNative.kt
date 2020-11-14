package io.github.hadixlin.iss.win

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.win32.W32APIOptions

/**
 * @author hadix
 */
interface WinNative : User32 {

    fun GetKeyboardLayout(pid: WinDef.DWORD): WinNT.HANDLE

    companion object {
        val INSTANCE = Native.load(
            "user32",
            WinNative::class.java,
            W32APIOptions.DEFAULT_OPTIONS
        ) as WinNative
    }

}
