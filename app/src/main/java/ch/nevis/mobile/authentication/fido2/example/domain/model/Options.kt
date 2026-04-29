package ch.nevis.mobile.authentication.fido2.example.domain.model

/**
 * Options controlling the FIDO2 passkey registration ceremony.
 *
 * These values map to the corresponding WebAuthn `PublicKeyCredentialCreationOptions` fields
 * sent to the Nevis Authentication Cloud server.
 *
 * @property userVerificationRequirement Whether user verification (e.g. biometric, PIN) is required.
 * @property authenticatorAttachment Preferred attachment type of the authenticator.
 * @property attestationConveyancePreference How the attestation statement should be conveyed.
 * @property residentKey Whether a resident/discoverable credential is required.
 */
data class RegistrationOptions(
    val userVerificationRequirement: UserVerificationRequirement = UserVerificationRequirement.REQUIRED,
    val authenticatorAttachment: AuthenticatorAttachment = AuthenticatorAttachment.PLATFORM,
    val attestationConveyancePreference: AttestationConveyancePreference = AttestationConveyancePreference.UNSPECIFIED,
    val residentKey: ResidentKey = ResidentKey.REQUIRED,
)

/**
 * Options controlling the FIDO2 passkey authentication ceremony.
 *
 * @property userVerificationRequirement Whether user verification is required during assertion.
 */
data class AuthenticationOptions(
    val userVerificationRequirement: UserVerificationRequirement = UserVerificationRequirement.REQUIRED,
)

/**
 * Specifies whether and how user verification is required during a FIDO2 ceremony.
 *
 * Maps to the WebAuthn `UserVerificationRequirement` enum values.
 */
enum class UserVerificationRequirement(
    val value: String,
) {
    /** Indicates no preference; the server will decide. */
    UNSPECIFIED("unspecified"),
    /** User verification is preferred but not required. */
    PREFERRED("preferred"),
    /** User verification is required. The ceremony will fail without it. */
    REQUIRED("required"),
    /** User verification should not be performed. */
    DISCOURAGED("discouraged");
}

/**
 * Specifies the preferred authenticator attachment modality.
 *
 * Maps to the WebAuthn `AuthenticatorAttachment` enum values.
 */
enum class AuthenticatorAttachment(
    val value: String,
) {
    /** No preference; any authenticator may be used. */
    UNSPECIFIED("unspecified"),
    /** A platform authenticator (e.g. device biometric, face recognition) is preferred. */
    PLATFORM("platform"),
    /** A roaming authenticator (e.g. security key) is preferred. */
    CROSS_PLATFORM("cross-platform");
}

/**
 * Specifies how the attestation statement is conveyed to the relying party.
 *
 * Maps to the WebAuthn `AttestationConveyancePreference` enum values.
 */
enum class AttestationConveyancePreference(
    val value: String,
) {
    /** No preference; the authenticator may or may not provide attestation. */
    UNSPECIFIED("unspecified"),
    /** No attestation data is sent. */
    NONE("none"),
    /** The authenticator's attestation is verifiable by the server, potentially anonymised. */
    INDIRECT("indirect"),
    /** The authenticator's attestation is directly verifiable by the server. */
    DIRECT("direct");
}

/**
 * Specifies whether a resident key (discoverable credential) should be created.
 *
 * Maps to the WebAuthn `ResidentKeyRequirement` enum values.
 */
enum class ResidentKey(
    val value: String,
) {
    /** No preference; the platform decides. */
    UNSPECIFIED("unspecified"),
    /** A resident key is preferred but not required. */
    PREFERRED("preferred"),
    /** A resident key is required for discoverable-credential / usernameless authentication. */
    REQUIRED("required"),
    /** A resident key should not be created. */
    DISCOURAGED("discouraged");
}
