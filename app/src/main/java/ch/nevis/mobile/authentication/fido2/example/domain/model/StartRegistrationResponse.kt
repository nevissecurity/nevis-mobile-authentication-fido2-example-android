package ch.nevis.mobile.authentication.fido2.example.domain.model

import ch.nevis.mobile.authentication.fido2.example.data.model.CredentialCreationOptions

/**
 * Domain model representing the server's response to a start-registration request.
 *
 * Carries the WebAuthn challenge and the status token required by the second phase.
 *
 * @property username The username being enrolled, echoed back for correlation.
 * @property statusToken Opaque token that must be included in [CompleteRegistrationRequest].
 * @property credentialCreationOptions The WebAuthn `PublicKeyCredentialCreationOptions` challenge
 *           to be forwarded to Android Credential Manager.
 */
data class StartRegistrationResponse(
    val username: String,
    val statusToken: String,
    val credentialCreationOptions: CredentialCreationOptions,
)
