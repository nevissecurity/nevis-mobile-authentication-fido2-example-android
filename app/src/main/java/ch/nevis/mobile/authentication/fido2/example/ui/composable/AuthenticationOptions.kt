package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.nevis.mobile.authentication.fido2.example.R
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.UserVerificationRequirement

/**
 * Composable that renders the FIDO2 authentication options selector.
 *
 * Displays a segmented button row for the [UserVerificationRequirement] enum.
 *
 * @param modifier Modifier to apply to this composable.
 * @param currentAuthenticationOptions The currently selected authentication options.
 * @param onAuthenticationOptionsChange Callback invoked when the user changes an option.
 */
@Composable
fun AuthenticationOptions(
    modifier: Modifier = Modifier,
    currentAuthenticationOptions: AuthenticationOptions = AuthenticationOptions(),
    onAuthenticationOptionsChange: (AuthenticationOptions) -> Unit
) {
    Column(modifier = modifier) {
        Text(stringResource(R.string.authentication_options_user_verification_requirement_title))
        Options(
            UserVerificationRequirement.entries.map { it.value },
            currentAuthenticationOptions.userVerificationRequirement.ordinal
        ) {
            onAuthenticationOptionsChange(
                currentAuthenticationOptions.copy(
                    userVerificationRequirement = UserVerificationRequirement.entries[it]
                )
            )
        }
    }
}

@Composable
@Preview
fun AuthenticationOptionsPreview() {
    AuthenticationOptions {}
}
