package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the WebAuthn `PublicKeyCredentialRequestOptions` returned by the
 * Authentication Cloud approval endpoint.
 *
 * This object is serialised to JSON and passed directly to [GetPublicKeyCredentialOption]
 * for Android Credential Manager.
 *
 * @property challenge Base64URL-encoded random challenge.
 * @property timeout Maximum time in milliseconds the user has to respond.
 * @property rpId The relying party identifier.
 * @property allowCredentials List of acceptable credential descriptors. An empty list means
 *           any registered credential for the RP is acceptable (discoverable credential flow).
 * @property userVerification The user verification requirement string.
 */
data class CredentialRequestOptions(
    val challenge: String,
    val timeout: Int? = null,
    val rpId: String? = null,
    val allowCredentials: Array<Credential> = emptyArray<Credential>(),
    val userVerification: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CredentialRequestOptions

        if (timeout != other.timeout) return false
        if (challenge != other.challenge) return false
        if (rpId != other.rpId) return false
        if (!allowCredentials.contentEquals(other.allowCredentials)) return false
        if (userVerification != other.userVerification) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timeout ?: 0
        result = 31 * result + challenge.hashCode()
        result = 31 * result + (rpId?.hashCode() ?: 0)
        result = 31 * result + allowCredentials.contentHashCode()
        result = 31 * result + (userVerification?.hashCode() ?: 0)
        return result
    }
}
