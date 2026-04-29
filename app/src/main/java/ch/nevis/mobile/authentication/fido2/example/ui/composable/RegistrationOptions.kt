package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.nevis.mobile.authentication.fido2.example.R
import ch.nevis.mobile.authentication.fido2.example.domain.model.AttestationConveyancePreference
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticatorAttachment
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.ResidentKey
import ch.nevis.mobile.authentication.fido2.example.domain.model.UserVerificationRequirement

/**
 * Composable that renders the full set of FIDO2 registration options selectors.
 *
 * Displays segmented button rows for [UserVerificationRequirement], [AuthenticatorAttachment],
 * [AttestationConveyancePreference], and [ResidentKey].
 *
 * @param modifier Modifier to apply to the root column.
 * @param currentRegistrationOptions The currently selected registration options.
 * @param onRegistrationOptionsChange Callback invoked when the user changes any option.
 */
@Composable
fun RegistrationOptions(
    modifier: Modifier = Modifier,
    currentRegistrationOptions: RegistrationOptions = RegistrationOptions(),
    onRegistrationOptionsChange: (RegistrationOptions) -> Unit
) {
    Column(modifier = modifier) {
        Text(stringResource(R.string.registration_options_user_verification_requirement_title))
        Options(
            UserVerificationRequirement.entries.map { it.value },
            currentRegistrationOptions.userVerificationRequirement.ordinal
        ) {
            onRegistrationOptionsChange(
                currentRegistrationOptions.copy(userVerificationRequirement = UserVerificationRequirement.entries[it])
            )
        }

        Text(stringResource(R.string.registration_options_authenticator_attachment_title))
        Options(
            AuthenticatorAttachment.entries.map { it.value },
            currentRegistrationOptions.authenticatorAttachment.ordinal
        ) {
            onRegistrationOptionsChange(
                currentRegistrationOptions.copy(authenticatorAttachment = AuthenticatorAttachment.entries[it])
            )
        }

        Text(stringResource(R.string.registration_options_attestation_conveyance_preference_title))
        Options(
            AttestationConveyancePreference.entries.map { it.value },
            currentRegistrationOptions.attestationConveyancePreference.ordinal
        ) {
            onRegistrationOptionsChange(
                currentRegistrationOptions.copy(attestationConveyancePreference = AttestationConveyancePreference.entries[it])
            )
        }

        Text(stringResource(R.string.registration_options_resident_key_title))
        Options(
            ResidentKey.entries.map { it.value },
            currentRegistrationOptions.residentKey.ordinal
        ) {
            onRegistrationOptionsChange(
                currentRegistrationOptions.copy(residentKey = ResidentKey.entries[it])
            )
        }
    }
}

@Composable
@Preview
fun RegistrationOptionsPreview() {
    RegistrationOptions {}
}
