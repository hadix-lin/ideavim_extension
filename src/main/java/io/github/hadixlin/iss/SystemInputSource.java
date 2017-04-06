package io.github.hadixlin.iss;

/**
 * Created by hadix on 28/03/2017.
 */
public class SystemInputSource {

    private static final String DEFAULT_INPUT_SOURCE = "com.apple.keylayout.US";

    public static void switchToEnglish() {
        InputSourceSwitcher.switchInputSource(DEFAULT_INPUT_SOURCE);
    }
}