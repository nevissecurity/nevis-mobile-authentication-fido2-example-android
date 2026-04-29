package ch.nevis.mobile.authentication.fido2.example.data.model

import ch.nevis.mobile.authentication.fido2.example.domain.model.AttestationConveyancePreference
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticatorAttachment
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.ResidentKey
import ch.nevis.mobile.authentication.fido2.example.domain.model.UserVerificationRequirement

/**
 * Data-layer DTO representing the FIDO2 options that are sent to the Authentication Cloud API
 * in both the enrollment and approval requests.
 *
 * @property authenticatorSelection Authenticator selection criteria (attachment, resident key,
 *           user verification).
 * @property attestation The attestation conveyance preference string, or `null` if unspecified.
 * @property userVerification The user verification requirement string, or `null` if unspecified.
 */
data class Fido2Options(
    val authenticatorSelection: AuthenticatorSelection? = null,
    val attestation: String? = null,
    val userVerification: String? = null,
) {
    companion object {
        /**
         * Converts a domain-layer [RegistrationOptions] to a [Fido2Options] DTO suitable
         * for inclusion in a [UserEnrollmentRequest].
         *
         * @param registrationOptions The domain model to map.
         * @return The corresponding [Fido2Options] DTO.
         */
        fun from(registrationOptions: RegistrationOptions): Fido2Options {
            return Fido2Options(
                authenticatorSelection = AuthenticatorSelection(
                    registrationOptions.residentKey == ResidentKey.REQUIRED || registrationOptions.residentKey == ResidentKey.PREFERRED,
                    if (registrationOptions.residentKey != ResidentKey.UNSPECIFIED) registrationOptions.residentKey.value else null,
                    if (registrationOptions.userVerificationRequirement != UserVerificationRequirement.UNSPECIFIED) registrationOptions.userVerificationRequirement.value else null,
                    if (registrationOptions.authenticatorAttachment != AuthenticatorAttachment.UNSPECIFIED) registrationOptions.authenticatorAttachment.value else null
                ),
                attestation = if (registrationOptions.attestationConveyancePreference != AttestationConveyancePreference.UNSPECIFIED) registrationOptions.attestationConveyancePreference.value else null,
                userVerification = null
            )
        }

        /**
         * Converts a domain-layer [AuthenticationOptions] to a [Fido2Options] DTO suitable
         * for inclusion in an [ApprovalRequest].
         *
         * @param authenticationOptions The domain model to map.
         * @return The corresponding [Fido2Options] DTO.
         */
        fun from(authenticationOptions: AuthenticationOptions): Fido2Options {
            return Fido2Options(
                authenticatorSelection = null,
                attestation = null,
                userVerification = if (authenticationOptions.userVerificationRequirement != UserVerificationRequirement.UNSPECIFIED) authenticationOptions.userVerificationRequirement.value else null
            )
        }
    }
}
