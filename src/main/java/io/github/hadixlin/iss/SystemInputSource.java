package io.github.hadixlin.iss;

import static io.github.hadixlin.iss.InputSourceSwitcher.getCurrentInputSourceID;

/**
 * Created by hadix on 28/03/2017.
 */
public class SystemInputSource {

    private static final String DEFAULT_INPUT_SOURCE = "com.apple.keylayout.US";
    public static String formerInputSource = "com.apple.keylayout.US";

    public static void switchToEnglish() {
        updateFormer();
        InputSourceSwitcher.switchInputSource(DEFAULT_INPUT_SOURCE);
    }

    public static void switchToFormer() {
        if (!formerInputSource.equals(getCurrentInputSourceID())) {
            InputSourceSwitcher.switchInputSource(formerInputSource);
        }
    }

    public static void updateFormer() {
        formerInputSource = getCurrentInputSourceID();
    }
}