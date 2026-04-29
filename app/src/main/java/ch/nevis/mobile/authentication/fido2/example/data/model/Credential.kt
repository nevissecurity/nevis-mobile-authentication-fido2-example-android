package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing a WebAuthn `PublicKeyCredentialDescriptor` used in credential lists
 * (e.g. `excludeCredentials` in [CredentialCreationOptions] or `allowCredentials`
 * in [CredentialRequestOptions]).
 *
 * @property type The credential type, always `"public-key"`.
 * @property id The Base64URL-encoded credential ID.
 */
data class Credential(
    val type: String,
    val id: String,
)
