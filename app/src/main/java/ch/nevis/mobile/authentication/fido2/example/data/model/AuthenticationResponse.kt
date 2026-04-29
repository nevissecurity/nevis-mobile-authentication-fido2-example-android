package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the authenticator assertion response returned by Android Credential Manager.
 *
 * This is a subset of the WebAuthn `AuthenticatorAssertionResponse` object, serialised for the
 * Authentication Cloud assertion endpoint.
 *
 * @property authenticatorData Base64URL-encoded authenticator data.
 * @property clientDataJSON Base64URL-encoded client data JSON.
 * @property signature Base64URL-encoded ECDSA signature over authenticatorData and clientDataJSON.
 * @property userHandle Base64URL-encoded user handle identifying the user, or `null` for
 *           non-discoverable credentials.
 */
data class AuthenticationResponse(
    val authenticatorData: String,
    val clientDataJSON: String,
    val signature: String,
    val userHandle: String?
)
