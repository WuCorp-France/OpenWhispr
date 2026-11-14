package com.edib.openwhispr

/** Pure visibility decision, separate from Android window lifecycle for unit tests. */
object OverlayVisibilityPolicy {
    fun shouldShow(
        enabled: Boolean,
        deviceUnlocked: Boolean,
        keyboardOnly: Boolean,
        keyboardVisible: Boolean,
        inputFocused: Boolean,
        dictationActive: Boolean,
    ): Boolean {
        if (!enabled || !deviceUnlocked) return false
        if (dictationActive) return true
        return if (keyboardOnly) keyboardVisible else keyboardVisible || inputFocused
    }
}
