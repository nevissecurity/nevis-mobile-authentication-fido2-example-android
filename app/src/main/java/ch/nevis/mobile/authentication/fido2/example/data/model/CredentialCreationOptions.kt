package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the WebAuthn `PublicKeyCredentialCreationOptions` returned by the
 * Authentication Cloud enrollment endpoint.
 *
 * This object is serialised to JSON and passed directly to [CreatePublicKeyCredentialRequest]
 * for Android Credential Manager.
 *
 * @property rp Relying party information.
 * @property user User information for the new credential.
 * @property challenge Base64URL-encoded random challenge.
 * @property pubKeyCredParams Acceptable public key algorithms and credential types.
 * @property timeout Maximum time in milliseconds the user has to respond.
 * @property authenticatorSelection Authenticator selection criteria.
 * @property attestation Attestation conveyance preference string.
 * @property excludeCredentials Previously registered credentials to exclude.
 */
data class CredentialCreationOptions(
    val rp: RelyingParty,
    val user: User,
    val challenge: String,
    val pubKeyCredParams: Array<PublicKeyCredentialParameters>,
    val timeout: Int,
    val authenticatorSelection: AuthenticatorSelection,
    val attestation: String,
    val excludeCredentials: Array<Credential>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CredentialCreationOptions

        if (timeout != other.timeout) return false
        if (rp != other.rp) return false
        if (user != other.user) return false
        if (challenge != other.challenge) return false
        if (!pubKeyCredParams.contentEquals(other.pubKeyCredParams)) return false
        if (authenticatorSelection != other.authenticatorSelection) return false
        if (attestation != other.attestation) return false
        if (!excludeCredentials.contentEquals(other.excludeCredentials)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timeout
        result = 31 * result + rp.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + challenge.hashCode()
        result = 31 * result + pubKeyCredParams.contentHashCode()
        result = 31 * result + authenticatorSelection.hashCode()
        result = 31 * result + attestation.hashCode()
        result = 31 * result + excludeCredentials.contentHashCode()
        return result
    }
}
