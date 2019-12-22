package io.github.hadixlin.iss.mac

import io.github.hadixlin.iss.InputMethodSwitcher
import io.github.hadixlin.iss.mac.MacNative.getCurrentInputSourceID
import io.github.hadixlin.iss.mac.MacNative.switchInputSource
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.StringUtils.EMPTY

/**
 * @author hadix
 * @date 2018-12-23
 */
class MacInputMethodSwitcher : InputMethodSwitcher {

    @Volatile
    private var lastInputSource: String = EMPTY

    override fun switchToEnglish() {
        val current = getCurrentInputSourceID()
        lastInputSource = current
        if (StringUtils.equals(current, ENGLISH_INPUT_SOURCE)) {
            return
        }
        val code = switchInputSource(ENGLISH_INPUT_SOURCE)
        if (code < 0) {
            ENGLISH_INPUT_SOURCE = KEY_LAYOUT_US
            switchInputSource(ENGLISH_INPUT_SOURCE)
        }
    }

    override fun restore() {
        if (lastInputSource == EMPTY) {
            return
        }
        switchInputSource(lastInputSource)
        lastInputSource = EMPTY
    }

    companion object {
        private const val KEY_LAYOUT_US = "com.apple.keylayout.US"
        private const val KEY_LAYOUT_ABC = "com.apple.keylayout.ABC"
        private var ENGLISH_INPUT_SOURCE = KEY_LAYOUT_ABC
    }
}
