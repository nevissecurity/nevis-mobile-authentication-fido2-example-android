package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the WebAuthn `AuthenticatorSelectionCriteria` sent in the enrollment request.
 *
 * @property requireResidentKey Whether a resident key is required (legacy field, prefer [residentKey]).
 * @property residentKey The resident key requirement string (`"required"`, `"preferred"`, etc.),
 *           or `null` if unspecified.
 * @property userVerification The user verification requirement string, or `null` if unspecified.
 * @property authenticatorAttachment The preferred authenticator attachment (`"platform"`,
 *           `"cross-platform"`), or `null` if unspecified.
 */
data class AuthenticatorSelection(
    var requireResidentKey: Boolean,
    var residentKey: String?,
    val userVerification: String?,
    val authenticatorAttachment: String?,
)
