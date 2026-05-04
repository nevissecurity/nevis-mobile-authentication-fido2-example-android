package ch.nevis.mobile.authentication.fido2.example.domain.model

/**
 * Domain model carrying the JWT access token to be validated via the introspection endpoint.
 *
 * @property token The raw JWT string to validate.
 */
data class IntrospectRequest(
    val token: String,
)
