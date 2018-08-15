package io.github.hadixlin.iss;

/** Created by hadix on 28/03/2017. */
public class SystemInputSource {

  public static final String KEY_LAYOUT_US = "com.apple.keylayout.US";
  public static final String KEY_LAYOUT_ABC = "com.apple.keylayout.ABC";
  static String ENGLISH_INPUT_SOURCE = KEY_LAYOUT_ABC;

  public static void switchToEnglish() {
    int code = switchTo(ENGLISH_INPUT_SOURCE);
    if (code < 0) {
      ENGLISH_INPUT_SOURCE = KEY_LAYOUT_US;
      switchTo(ENGLISH_INPUT_SOURCE);
    }
  }

  public static int switchTo(String inputSourceId) {
    return InputSourceSwitcher.switchInputSource(inputSourceId);
  }

  public static String getCurrentInputSource() {
    return InputSourceSwitcher.getCurrentInputSourceID();
  }

  public static void main(String[] args) {
    switchToEnglish();
  }
}
