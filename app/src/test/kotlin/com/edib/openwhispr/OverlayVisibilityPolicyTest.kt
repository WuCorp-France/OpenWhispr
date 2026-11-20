package com.edib.openwhispr

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayVisibilityPolicyTest {
    private fun visible(
        enabled: Boolean = true, deviceUnlocked: Boolean = true,
        keyboardOnly: Boolean = true, keyboardVisible: Boolean = false,
        inputFocused: Boolean = false, dictationActive: Boolean = false,
    ) = OverlayVisibilityPolicy.shouldShow(
        enabled, deviceUnlocked, keyboardOnly, keyboardVisible, inputFocused, dictationActive
    )

    @Test fun alwaysOnDisplayNeverShowsBubble() {
        assertFalse(visible(deviceUnlocked = false, keyboardVisible = true, dictationActive = true))
    }
    @Test fun hidesWhenKeyboardClosesEvenIfEditTextKeepsFocus() {
        assertFalse(visible(inputFocused = true))
    }
    @Test fun showsWhenKeyboardVisible() {
        assertTrue(visible(keyboardVisible = true))
    }
    @Test fun keepsBubbleVisibleDuringActiveDictationWhileUnlocked() {
        assertTrue(visible(dictationActive = true))
    }
    @Test fun regressionNoMicWhenHyperOsCannotReportFocusedImeWindow() {
        assertTrue(visible(keyboardOnly = false, inputFocused = true, keyboardVisible = false))
    }
    @Test fun keyboardClosedSuppressesStaleEditorFocus() {
        assertFalse(OverlayVisibilityPolicy.shouldShow(
            enabled = true,
            deviceUnlocked = true,
            keyboardOnly = false,
            keyboardVisible = false,
            inputFocused = true,
            dictationActive = false,
            keyboardDismissed = true
        ))
    }
    @Test fun openingKeyboardAgainOverridesDismissal() {
        assertTrue(OverlayVisibilityPolicy.shouldShow(
            enabled = true,
            deviceUnlocked = true,
            keyboardOnly = false,
            keyboardVisible = true,
            inputFocused = true,
            dictationActive = false,
            keyboardDismissed = true
        ))
    }
    @Test fun strictKeyboardModeDoesNotUseFocusFallback() {
        assertFalse(visible(keyboardOnly = true, inputFocused = true, keyboardVisible = false))
    }
    @Test fun optionalCompatibilityModeAllowsFocusedFieldsWithoutKeyboard() {
        assertTrue(visible(keyboardOnly = false, inputFocused = true))
    }
    @Test fun backgroundServiceOffAlwaysHidesBubble() {
        assertFalse(visible(enabled = false, keyboardVisible = true, dictationActive = true))
    }
    @Test fun screenLockedAlwaysHidesEvenWithoutKeyboardRestriction() {
        assertFalse(visible(deviceUnlocked = false, keyboardOnly = false, inputFocused = true))
    }
}
