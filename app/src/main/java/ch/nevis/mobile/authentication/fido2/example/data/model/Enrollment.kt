package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the enrollment details nested inside a [UserEnrollmentResponse].
 *
 * @property transactionId Unique identifier of the enrollment transaction.
 * @property statusToken Opaque token required for the subsequent attestation call.
 * @property credentialCreationOptions The WebAuthn challenge to pass to Android Credential Manager.
 */
data class Enrollment(
    val transactionId: String,
    val statusToken: String,
    val credentialCreationOptions: CredentialCreationOptions,
)
