package io.github.hadixlin.iss;

import static io.github.hadixlin.iss.InputSourceSwitcher.getCurrentInputSourceID;
import static io.github.hadixlin.iss.InputSourceSwitcher.switchInputSource;

/**
 * Created by hadix on 28/03/2017.
 */
public class SystemInputSource {

    static final String ENGLISH_INPUT_SOURCE = "com.apple.keylayout.US";

    public static void switchToEnglish() {
        switchTo(ENGLISH_INPUT_SOURCE);
    }

    public static void switchTo(String inputSourceId) {
        switchInputSource(inputSourceId);
    }

    public static String getCurrentInputSource() {
        return getCurrentInputSourceID();
    }
}