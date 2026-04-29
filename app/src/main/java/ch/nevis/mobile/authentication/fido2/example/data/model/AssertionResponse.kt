package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO for the `POST _app/assertion/result/` response body.
 *
 * @property errorMessage Non-null only when the assertion failed; contains a human-readable
 *           error description.
 * @property status The outcome status string (e.g. `"success"`).
 * @property token The JWT access token issued on successful authentication, or `null` on failure.
 */
data class AssertionResponse(
    val errorMessage: String? = null,
    val status: String? = null,
    val token: String? = null,
)
