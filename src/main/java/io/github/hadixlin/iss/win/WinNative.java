package io.github.hadixlin.iss.win;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

public interface WinNative extends User32 {
    WinNative INSTANCE = (WinNative) Native.loadLibrary("user32", WinNative.class, W32APIOptions.DEFAULT_OPTIONS);

     HANDLE GetKeyboardLayout(DWORD pid);


}
