package ch.nevis.mobile.authentication.fido2.example.data.model

import ch.nevis.mobile.authentication.fido2.example.data.util.UserAgentProvider

/**
 * DTO for the `POST _app/attestation/result/` request body.
 *
 * Carries the WebAuthn attestation response produced by Android Credential Manager
 * after the user creates a passkey.
 *
 * @property id The Base64URL-encoded credential ID of the newly created passkey.
 * @property response Attestation object and client data JSON from the registration ceremony.
 * @property statusToken The opaque token returned by the enrollment endpoint.
 * @property type The credential type, typically `"public-key"`.
 * @property userFriendlyName Optional human-readable label for the authenticator.
 * @property userAgent The HTTP User-Agent string, auto-populated from system properties.
 */
data class AttestationRequest(
    val id: String,
    val response: EnrollmentResponse,
    val statusToken: String,
    val type: String,
    val userFriendlyName: String? = null,
    val userAgent: String? = UserAgentProvider.get()
)
