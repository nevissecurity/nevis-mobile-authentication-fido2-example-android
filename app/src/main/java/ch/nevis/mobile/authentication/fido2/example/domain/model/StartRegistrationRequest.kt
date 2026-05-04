package ch.nevis.mobile.authentication.fido2.example.domain.model

/**
 * Domain model representing the input for the first phase of the passkey registration ceremony.
 *
 * @property username The username to enroll on the Authentication Cloud backend.
 * @property registrationOptions FIDO2 options to forward to the server.
 */
data class StartRegistrationRequest(
    val username: String,
    val registrationOptions: RegistrationOptions
)
