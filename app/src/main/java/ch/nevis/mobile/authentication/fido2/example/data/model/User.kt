package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the WebAuthn `PublicKeyCredentialUserEntity` sent in [CredentialCreationOptions].
 *
 * @property id The Base64URL-encoded user handle.
 * @property displayName Human-readable display name for the user.
 * @property name The username.
 */
data class User(
    val id: String,
    val displayName: String,
    val name: String,
)
