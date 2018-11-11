package io.github.hadixlin.iss.mac;

import com.sun.jna.Native;

/** Created by hadix on 30/03/2017. */
public class MacNative {

  static {
    Native.register("input-source-switcher");
  }

  public static native String getCurrentInputSourceID();

  public static native int switchInputSource(String inputSourceID);
}
