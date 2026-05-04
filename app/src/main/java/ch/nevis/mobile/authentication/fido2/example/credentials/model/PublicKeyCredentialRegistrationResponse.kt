package ch.nevis.mobile.authentication.fido2.example.credentials.model

/**
 * Deserialized representation of the JSON registration response returned by Android Credential
 * Manager via [CreatePublicKeyCredentialResponse.registrationResponseJson].
 *
 * Maps to the WebAuthn `RegistrationResponseJSON` structure.
 *
 * @property rawId Base64URL-encoded raw credential ID.
 * @property authenticatorAttachment The attachment modality of the authenticator.
 * @property type The credential type, always `"public-key"`.
 * @property id The Base64URL-encoded credential ID (same as [rawId] in most cases).
 * @property response The attestation response containing the attestation object and client data JSON.
 */
data class PublicKeyCredentialRegistrationResponse(
    val rawId: String,
    val authenticatorAttachment: String,
    val type: String,
    val id: String,
    val response: AuthenticatorAttestationResponse,
)
