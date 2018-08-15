package io.github.hadixlin.iss;

import com.sun.jna.Native;

/** Created by hadix on 30/03/2017. */
public class InputSourceSwitcher {

  static {
    Native.register("input-source-switcher");
  }

  public static native String getCurrentInputSourceID();

  public static native int switchInputSource(String inputSourceID);
}
