package ch.nevis.mobile.authentication.fido2.example.domain.model

import java.util.Date

/**
 * Domain model representing the validated token claims returned by the introspection endpoint.
 *
 * @property isActive Whether the token is currently active and not expired.
 * @property issuedAt The time at which the token was issued.
 * @property expirationTime The time at which the token expires.
 * @property notBefore The earliest time at which the token is valid.
 * @property subject The principal that is the subject of the token (typically the username).
 * @property audience The intended audience of the token.
 * @property issuer The token issuer.
 * @property jwtId The unique identifier of the JWT.
 * @property scope The scope of access granted by the token.
 */
data class IntrospectResponse(
    val isActive: Boolean,
    val issuedAt: Date?,
    val expirationTime: Date?,
    val notBefore: String?,
    val subject: String?,
    val audience: String?,
    val issuer: String?,
    val jwtId: String?,
    val scope: String?,
) {
    override fun toString(): String {
        val stringBuffer = StringBuilder()
        stringBuffer.append("Issued at", issuedAt?.toString())
        stringBuffer.append("Expiration time", expirationTime?.toString())
        stringBuffer.append("Subject", subject)
        stringBuffer.append("Audience", audience)
        stringBuffer.append("Issuer", issuer)
        stringBuffer.append("JWT ID", jwtId)
        return stringBuffer.toString()
    }
}

private fun StringBuilder.append(label: String, value: String?) {
    if (value != null) {
        append("$label: $value\n")
    }
}
