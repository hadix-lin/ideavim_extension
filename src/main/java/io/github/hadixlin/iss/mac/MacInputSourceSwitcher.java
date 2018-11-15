package io.github.hadixlin.iss.mac;

import io.github.hadixlin.iss.InputSourceSwitcher;
import org.apache.commons.lang.StringUtils;

import static io.github.hadixlin.iss.mac.MacNative.*;

public class MacInputSourceSwitcher implements InputSourceSwitcher {
    public static final String KEY_LAYOUT_US = "com.apple.keylayout.US";
    public static final String KEY_LAYOUT_ABC = "com.apple.keylayout.ABC";
    public static String ENGLISH_INPUT_SOURCE = KEY_LAYOUT_ABC;

    private String lastInputSource = null;

    @Override
    public void switchToEnglish() {
        String current = getCurrentInputSourceID();
        lastInputSource = current;
        if (StringUtils.equals(current, ENGLISH_INPUT_SOURCE)) {
            return;
        }
        int code = switchInputSource(ENGLISH_INPUT_SOURCE);
        if (code < 0) {
            ENGLISH_INPUT_SOURCE = KEY_LAYOUT_US;
            switchInputSource(ENGLISH_INPUT_SOURCE);
        }
    }

    @Override
    public void restore() {
        String current = getCurrentInputSourceID();
        if (lastInputSource == null || StringUtils.equals(lastInputSource, current)) {
            return;
        }
        switchInputSource(lastInputSource);
    }
}
