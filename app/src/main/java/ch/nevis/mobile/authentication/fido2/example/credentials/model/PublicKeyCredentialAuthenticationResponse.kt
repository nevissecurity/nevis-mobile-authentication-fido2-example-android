package ch.nevis.mobile.authentication.fido2.example.credentials.model

/**
 * Deserialized representation of the JSON authentication response returned by Android Credential
 * Manager via [PublicKeyCredential.authenticationResponseJson].
 *
 * Maps to the WebAuthn `AuthenticationResponseJSON` structure.
 *
 * @property id The Base64URL-encoded credential ID.
 * @property rawId Base64URL-encoded raw credential ID.
 * @property response The assertion response containing authenticator data, signature, and client data JSON.
 * @property clientExtensionResults WebAuthn client extension outputs.
 * @property type The credential type, always `"public-key"`.
 * @property authenticatorAttachment The attachment modality of the authenticator, if reported.
 */
data class PublicKeyCredentialAuthenticationResponse(
    val id: String,
    val rawId: String,
    val response: AuthenticatorAssertionResponse,
    val clientExtensionResults: AuthenticationExtensionsClientOutputs,
    val type: String,
    val authenticatorAttachment: String? = null,
)
