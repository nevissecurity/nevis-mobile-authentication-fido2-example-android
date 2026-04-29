package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO for the `POST _app/attestation/result/` response body.
 *
 * @property errorMessage Non-null only when the attestation failed; contains a human-readable
 *           error description.
 * @property status The outcome status string (e.g. `"success"`).
 * @property token The JWT access token, if issued as part of the registration flow.
 */
data class AttestationResponse(
    val errorMessage: String? = null,
    val status: String? = null,
    val token: String? = null,
)
