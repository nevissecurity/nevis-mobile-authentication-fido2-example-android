package ch.nevis.mobile.authentication.fido2.example.data.util

/**
 * Provides a human-readable name for the device or authenticator, used as a label
 * when registering a passkey with the Authentication Cloud backend.
 */
interface UserFriendlyNameProvider {

    /**
     * Returns a human-readable authenticator name combining the device model
     * and the current date/time.
     */
    fun get(): String
}
