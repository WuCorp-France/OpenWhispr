package com.edib.openwhispr

/** Pure visibility decision, separate from Android window lifecycle for unit tests.
 * On some devices the accessibility IME window isn't marked focused/active.
 * A genuinely focused editor must still be able to reveal the microphone.
 */
object OverlayVisibilityPolicy {
    fun shouldShow(
        enabled: Boolean,
        deviceUnlocked: Boolean,
        keyboardOnly: Boolean,
        keyboardVisible: Boolean,
        inputFocused: Boolean,
        dictationActive: Boolean,
        keyboardDismissed: Boolean = false,
    ): Boolean {
        if (!enabled || !deviceUnlocked) return false
        if (dictationActive) return true
        if (keyboardVisible) return true
        if (keyboardOnly) return false
        // When an IME was previously detected and has disappeared, a stale
        // editable focus must not keep the overlay on top of other apps.
        return inputFocused && !keyboardDismissed
    }
}
