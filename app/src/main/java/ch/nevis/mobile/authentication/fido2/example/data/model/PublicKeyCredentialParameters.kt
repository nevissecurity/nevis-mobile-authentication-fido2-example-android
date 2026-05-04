package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing a `PublicKeyCredentialParameters` entry in the WebAuthn
 * `pubKeyCredParams` array from [CredentialCreationOptions].
 *
 * @property alg The COSE algorithm identifier (e.g. `-7` for ES256).
 * @property type The credential type, always `"public-key"`.
 */
data class PublicKeyCredentialParameters(
    val alg: Int,
    val type: String,
)
