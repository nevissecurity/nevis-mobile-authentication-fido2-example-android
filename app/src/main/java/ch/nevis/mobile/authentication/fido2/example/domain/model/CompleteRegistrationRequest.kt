package ch.nevis.mobile.authentication.fido2.example.domain.model

import ch.nevis.mobile.authentication.fido2.example.credentials.model.PublicKeyCredentialRegistrationResponse

/**
 * Domain model representing the input for the second phase of the passkey registration ceremony.
 *
 * @property username The username being enrolled.
 * @property userFriendlyName A human-readable device/authenticator label
 *           (e.g. "Pixel 8 2025-01-15 10:30").
 * @property statusToken The opaque token returned by [StartRegistrationResponse].
 * @property publicKeyCredentialRegistrationResponse The attestation object returned by Android
 *           Credential Manager after the user creates a passkey.
 */
data class CompleteRegistrationRequest(
    val username: String,
    val userFriendlyName: String,
    val statusToken: String,
    val publicKeyCredentialRegistrationResponse: PublicKeyCredentialRegistrationResponse,
)
