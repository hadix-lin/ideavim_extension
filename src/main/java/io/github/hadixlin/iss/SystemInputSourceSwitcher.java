package io.github.hadixlin.iss;

import io.github.hadixlin.iss.mac.MacInputSourceSwitcher;
import io.github.hadixlin.iss.win.WinInputSourceSwitcher;
import org.apache.commons.lang.SystemUtils;

/** Created by hadix on 28/03/2017. */
public class SystemInputSourceSwitcher implements InputSourceSwitcher {
  private InputSourceSwitcher delegate;

  public SystemInputSourceSwitcher() {
    if(SystemUtils.IS_OS_WINDOWS){
      delegate = new WinInputSourceSwitcher();
    }else if(SystemUtils.IS_OS_MAC){
      delegate = new MacInputSourceSwitcher();
    }else{
      throw new IllegalArgumentException("Not Support Current System OS, Only Support Windows And MacOS");
    }
  }

  @Override
  public void switchToEnglish() {
    delegate.switchToEnglish();
  }

  @Override
  public void restore() {
    delegate.restore();
  }
}
