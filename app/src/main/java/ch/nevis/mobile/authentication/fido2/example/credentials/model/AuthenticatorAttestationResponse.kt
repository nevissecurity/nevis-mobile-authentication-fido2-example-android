package ch.nevis.mobile.authentication.fido2.example.credentials.model

/**
 * Deserialized representation of the attestation response returned by Android Credential Manager
 * as part of the passkey registration ceremony.
 *
 * Maps to the WebAuthn `AuthenticatorAttestationResponse` structure.
 *
 * @property clientDataJSON Base64URL-encoded JSON containing the challenge, origin, and type.
 * @property attestationObject Base64URL-encoded CBOR-encoded COSE attestation object.
 * @property transports The transport mechanisms supported by the authenticator (e.g. `["internal"]`).
 * @property authenticatorData Base64URL-encoded raw authenticator data.
 * @property publicKeyAlgorithm The COSE algorithm identifier of the credential public key
 *           (e.g. `-7` for ES256).
 * @property publicKey Base64URL-encoded COSE public key.
 */
data class AuthenticatorAttestationResponse(
    val clientDataJSON: String,
    val attestationObject: String,
    val transports: Array<String>,
    val authenticatorData: String,
    val publicKeyAlgorithm: Int,
    val publicKey: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthenticatorAttestationResponse

        if (publicKeyAlgorithm != other.publicKeyAlgorithm) return false
        if (clientDataJSON != other.clientDataJSON) return false
        if (attestationObject != other.attestationObject) return false
        if (!transports.contentEquals(other.transports)) return false
        if (authenticatorData != other.authenticatorData) return false
        if (publicKey != other.publicKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = publicKeyAlgorithm
        result = 31 * result + clientDataJSON.hashCode()
        result = 31 * result + attestationObject.hashCode()
        result = 31 * result + transports.contentHashCode()
        result = 31 * result + authenticatorData.hashCode()
        result = 31 * result + publicKey.hashCode()
        return result
    }
}
