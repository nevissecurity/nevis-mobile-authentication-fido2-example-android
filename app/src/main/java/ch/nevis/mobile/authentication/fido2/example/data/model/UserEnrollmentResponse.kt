package ch.nevis.mobile.authentication.fido2.example.data.model

import java.util.Date

/**
 * DTO for the `POST api/v1/users/enroll/` response body.
 *
 * @property userId The server-assigned user identifier.
 * @property username The enrolled username.
 * @property status The current enrollment status.
 * @property createdAt Timestamp of when the user record was created.
 * @property updatedAt Timestamp of the last update to the user record.
 * @property enrollment Contains the WebAuthn challenge and status token needed for attestation.
 */
data class UserEnrollmentResponse(
    val userId: String,
    val username: String,
    val status: String,
    val createdAt: Date,
    val updatedAt: Date,
    val enrollment: Enrollment,
)
