package ch.nevis.mobile.authentication.fido2.example.data.util

/**
 * Utility constants shared across data-layer classes.
 */
class Constants {
    companion object {
        /** Identifies the FIDO2 registration/authentication channel in Authentication Cloud API requests. */
        const val CHANNEL_FIDO2 = "fido2"
        /** Default resident key requirement value sent to the AC API when not overridden by options. */
        const val RESIDENT_KEY_REQUIRED = "required"
        /** Default user verification requirement value used in API requests. */
        const val USER_VERIFICATION_REQUIRED = "required"
    }
}
