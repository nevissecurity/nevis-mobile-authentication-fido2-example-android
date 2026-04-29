package ch.nevis.mobile.authentication.fido2.example.credentials.model

/**
 * Deserialized representation of the assertion response returned by Android Credential Manager
 * as part of the passkey authentication ceremony.
 *
 * Maps to the WebAuthn `AuthenticatorAssertionResponse` structure.
 *
 * @property clientDataJSON Base64URL-encoded JSON containing the challenge, origin, and type.
 * @property authenticatorData Base64URL-encoded raw authenticator data including sign count.
 * @property signature Base64URL-encoded ECDSA signature over authenticatorData and
 *           the SHA-256 hash of clientDataJSON.
 * @property userHandle Base64URL-encoded user handle, present for discoverable credentials.
 */
data class AuthenticatorAssertionResponse(
    val clientDataJSON: String,
    val authenticatorData: String,
    val signature: String,
    val userHandle: String? = null,
)
