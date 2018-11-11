package io.github.hadixlin.iss.win;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import io.github.hadixlin.iss.InputSourceSwitcher;

public class WinInputSourceSwitcher implements InputSourceSwitcher {
    private static final long KEY_LAYOUT_US = 0x4090409;

    private long lastInputSource = -1;

    @Override
    public void switchToEnglish() {
        WinDef.HWND hwnd = WinNative.INSTANCE.GetForegroundWindow();
        long current = getCurrentInputSource(hwnd);
        if (current == KEY_LAYOUT_US) {
            return;
        }
        switchToInputSource(hwnd, KEY_LAYOUT_US);
        lastInputSource = current;
    }

    private void switchToInputSource(WinDef.HWND hwnd, long inputSourceId) {
        WinNative.INSTANCE.PostMessage(hwnd, 0x0050, new WinDef.WPARAM(0), new WinDef.LPARAM(inputSourceId));
    }

    private static long getCurrentInputSource(WinDef.HWND hwnd) {
        int pid = WinNative.INSTANCE.GetWindowThreadProcessId(hwnd, null);
        WinNT.HANDLE hkl = WinNative.INSTANCE.GetKeyboardLayout(new WinDef.DWORD(pid));
        return Pointer.nativeValue(hkl.getPointer());
    }

    @Override
    public void restore() {
        WinDef.HWND hwnd = WinNative.INSTANCE.GetForegroundWindow();
        long current = getCurrentInputSource(hwnd);
        if (lastInputSource < 0 || lastInputSource == current) {
            return;
        }
        switchToInputSource(hwnd, lastInputSource);
    }

}
