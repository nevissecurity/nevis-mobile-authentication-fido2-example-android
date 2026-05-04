package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO for the `POST api/v1/introspect` form-encoded request.
 *
 * @property token The JWT access token to validate.
 */
data class IntrospectRequest(
    val token: String,
)
