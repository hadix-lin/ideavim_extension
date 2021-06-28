package io.github.hadixlin.iss.mac

import io.github.hadixlin.iss.InputMethodSwitcher
import io.github.hadixlin.iss.mac.MacNative.getCurrentInputSourceID
import io.github.hadixlin.iss.mac.MacNative.switchInputSource
import org.apache.commons.lang.StringUtils.EMPTY

/**
 * @author hadix
 * @date 2018-12-23
 */
class MacInputMethodSwitcher(
    private vararg val englishInputSourceCandidate: String = ENGLISH_INPUT_SOURCE_CANDIDATE
) : InputMethodSwitcher {
    @Volatile
    private var lastInputSource: String = EMPTY

    override fun storeCurrentThenSwitchToEnglish() {
        val current = getCurrentInputSourceID()
        lastInputSource = current
        if (ENGLISH_INPUT_SOURCE == current) {
            return
        }
        switchToEnglish()
    }

    override fun switchToEnglish() {
        if (ENGLISH_INPUT_SOURCE.isNotBlank()) {
            if (switchInputSource(ENGLISH_INPUT_SOURCE) < 0) {
                // 系统英文输入法可能发生变更，导致无法切换到记录到英文输入法
                ENGLISH_INPUT_SOURCE = EMPTY
            }
        } else {
            for (englishInputSource in englishInputSourceCandidate) {
                if (switchInputSource(englishInputSource) < 0) {
                    continue
                }else {
                    ENGLISH_INPUT_SOURCE = englishInputSource
                    break
                }
            }
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
        private const val KEY_LAYOUT_UNICODEHEX = "com.apple.keylayout.UnicodeHexInput"
        private val ENGLISH_INPUT_SOURCE_CANDIDATE = arrayOf(KEY_LAYOUT_UNICODEHEX, KEY_LAYOUT_ABC, KEY_LAYOUT_US)
        private var ENGLISH_INPUT_SOURCE: String = ""
    }
}
