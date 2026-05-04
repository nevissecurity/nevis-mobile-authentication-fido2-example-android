package ch.nevis.mobile.authentication.fido2.example.data.model

import java.util.Date

/**
 * DTO for the `POST api/v1/introspect` response body.
 *
 * Field names use the short JWT claim names as defined in RFC 7519.
 *
 * @property active Whether the token is currently active.
 * @property iat Issued-at time (Unix timestamp string).
 * @property exp Expiration time (Unix timestamp string).
 * @property nbf Not-before time (Unix timestamp string).
 * @property sub Subject claim (typically the username).
 * @property aud Audience claim.
 * @property iss Issuer claim.
 * @property jti JWT ID claim.
 * @property scope Space-separated list of scopes granted by the token.
 */
data class IntrospectResponse(
    val active: Boolean?,
    val iat: String?,
    val exp: String?,
    val nbf: String?,
    val sub: String?,
    val aud: String?,
    val iss: String?,
    val jti: String?,
    val scope: String?,
)

fun IntrospectResponse.toDomain(): ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse {
    return ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse(
        isActive = active ?: false,
        issuedAt = iat.toDate(),
        expirationTime = exp.toDate(),
        notBefore = nbf,
        subject = sub,
        audience = aud,
        issuer = iss,
        jwtId = jti,
        scope = scope,
    )
}

private fun String?.toDate(): Date? {
    return if (this != null) {
        Date(this.toLong())
    } else {
        null
    }
}
