package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the WebAuthn `PublicKeyCredentialRpEntity` (relying party information).
 *
 * @property id The relying party identifier (e.g. the RP's domain name).
 * @property name Human-readable name of the relying party.
 */
data class RelyingParty(
    val id: String,
    val name: String,
)
