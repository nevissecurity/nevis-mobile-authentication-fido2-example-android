package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ch.nevis.mobile.authentication.fido2.example.R
import ch.nevis.mobile.authentication.fido2.example.domain.model.AuthenticationOptions
import ch.nevis.mobile.authentication.fido2.example.domain.model.RegistrationOptions
import ch.nevis.mobile.authentication.fido2.example.ui.model.EmptyResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.ResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.SuccessResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.theme.NoPadding
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingLarge
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingMedium

/**
 * Composable that renders the main scrollable content of the home screen.
 *
 * Hosts expandable sections for registration, authentication, usernameless authentication,
 * and web-based authorization, as well as the result message and host label.
 *
 * @param modifier Modifier to apply to the root column.
 * @param currentUsername The currently entered username.
 * @param currentAuthenticationOptions The currently selected authentication options.
 * @param currentRegistrationOptions The currently selected registration options.
 * @param resultMessage The current result or error message to display.
 * @param host The Authentication Cloud host name shown at the bottom.
 * @param onUsernameChange Callback invoked when the username field changes.
 * @param onAuthenticationOptionsChange Callback invoked when authentication options change.
 * @param onRegistrationOptionsChange Callback invoked when registration options change.
 * @param onRegisterButtonClicked Callback invoked when the Register button is clicked.
 * @param onAuthenticateButtonClicked Callback invoked when the Authenticate button is clicked.
 * @param onAuthenticateUsernamelessButtonClicked Callback invoked when the usernameless Authenticate button is clicked.
 * @param onRegisterViaWebButtonClicked Callback invoked when the Register via Web button is clicked.
 * @param onAuthenticateViaWebButtonClicked Callback invoked when the Authenticate via Web button is clicked.
 */
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentUsername: String = "",
    currentAuthenticationOptions: AuthenticationOptions = AuthenticationOptions(),
    currentRegistrationOptions: RegistrationOptions = RegistrationOptions(),
    resultMessage: ResultMessage = EmptyResultMessage(),
    host: String = "",
    onUsernameChange: (String) -> Unit,
    onAuthenticationOptionsChange: (AuthenticationOptions) -> Unit,
    onRegistrationOptionsChange: (RegistrationOptions) -> Unit,
    onRegisterButtonClicked: () -> Unit,
    onAuthenticateButtonClicked: () -> Unit,
    onAuthenticateUsernamelessButtonClicked: () -> Unit,
    onRegisterViaWebButtonClicked: () -> Unit,
    onAuthenticateViaWebButtonClicked: () -> Unit,
) {
    var isRegistrationExpanded by remember { mutableStateOf(false) }
    var isRegistrationOptionsExpanded by remember { mutableStateOf(false) }
    var isAuthenticationExpanded by remember { mutableStateOf(false) }
    var isAuthenticationOptionsExpanded by remember { mutableStateOf(false) }
    var isAuthenticationUsernamelessExpanded by remember { mutableStateOf(false) }
    var isAuthorizationViaWebExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExpandableContainer(
            modifier = Modifier
                .padding(
                    start = PaddingMedium,
                    top = PaddingLarge,
                    end = PaddingMedium,
                    bottom = NoPadding,
                )
                .fillMaxWidth(),
            title = stringResource(R.string.home_registration_title),
            isExpanded = isRegistrationExpanded,
            onHeaderClicked = {
                isRegistrationExpanded = !isRegistrationExpanded
                if (isRegistrationExpanded) {
                    isAuthenticationExpanded = false
                    isAuthenticationUsernamelessExpanded = false
                    isAuthorizationViaWebExpanded = false
                }
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(PaddingMedium),
            ) {
                UsernameTextField(
                    Modifier.fillMaxWidth(),
                    currentUsername = currentUsername,
                    onUsernameChange = onUsernameChange,
                )
                ExpandableContainer(
                    title = stringResource(R.string.registration_options_options_title),
                    isExpanded = isRegistrationOptionsExpanded,
                    onHeaderClicked = {
                        isRegistrationOptionsExpanded = !isRegistrationOptionsExpanded
                    }) {
                    RegistrationOptions(
                        currentRegistrationOptions = currentRegistrationOptions,
                        onRegistrationOptionsChange = onRegistrationOptionsChange,
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRegisterButtonClicked
                ) {
                    Text(text = stringResource(R.string.home_registration_button_title))
                }
            }
        }

        ExpandableContainer(
            modifier = Modifier
                .padding(
                    horizontal = PaddingMedium,
                    vertical = NoPadding,
                )
                .fillMaxWidth(),
            title = stringResource(R.string.home_authentication_title),
            isExpanded = isAuthenticationExpanded,
            onHeaderClicked = {
                isAuthenticationExpanded = !isAuthenticationExpanded
                if (isAuthenticationExpanded) {
                    isRegistrationExpanded = false
                    isAuthenticationUsernamelessExpanded = false
                    isAuthorizationViaWebExpanded = false
                }
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(PaddingMedium),
            ) {
                UsernameTextField(
                    Modifier.fillMaxWidth(),
                    currentUsername = currentUsername,
                    onUsernameChange = onUsernameChange,
                )
                ExpandableContainer(
                    title = stringResource(R.string.registration_options_options_title),
                    isExpanded = isAuthenticationOptionsExpanded,
                    onHeaderClicked = {
                        isAuthenticationOptionsExpanded = !isAuthenticationOptionsExpanded
                    }) {
                    AuthenticationOptions(
                        currentAuthenticationOptions = currentAuthenticationOptions,
                        onAuthenticationOptionsChange = onAuthenticationOptionsChange,
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAuthenticateButtonClicked
                ) {
                    Text(text = stringResource(R.string.home_authentication_button_title))
                }
            }
        }

        ExpandableContainer(
            modifier = Modifier
                .padding(
                    horizontal = PaddingMedium,
                    vertical = NoPadding,
                )
                .fillMaxWidth(),
            title = stringResource(R.string.home_authentication_usernameless_title),
            isExpanded = isAuthenticationUsernamelessExpanded,
            onHeaderClicked = {
                isAuthenticationUsernamelessExpanded = !isAuthenticationUsernamelessExpanded
                if (isAuthenticationUsernamelessExpanded) {
                    isRegistrationExpanded = false
                    isAuthenticationExpanded = false
                    isAuthorizationViaWebExpanded = false
                }
            }
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAuthenticateUsernamelessButtonClicked
            ) {
                Text(text = stringResource(R.string.home_authentication_usernameless_button_title))
            }
        }

        ExpandableContainer(
            modifier = Modifier
                .padding(
                    horizontal = PaddingMedium,
                    vertical = NoPadding,
                )
                .fillMaxWidth(),
            title = stringResource(R.string.home_authorization_via_web_title),
            isExpanded = isAuthorizationViaWebExpanded,
            onHeaderClicked = {
                isAuthorizationViaWebExpanded = !isAuthorizationViaWebExpanded
                if (isAuthorizationViaWebExpanded) {
                    isRegistrationExpanded = false
                    isAuthenticationExpanded = false
                    isAuthenticationUsernamelessExpanded = false
                }
            }
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterViaWebButtonClicked
            ) {
                Text(text = stringResource(R.string.home_register_via_web_button_title))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAuthenticateViaWebButtonClicked
            ) {
                Text(text = stringResource(R.string.home_authenticate_via_web_button_title))
            }
        }

        ResultMessage(
            modifier = Modifier
                .padding(
                    horizontal = PaddingMedium,
                    vertical = NoPadding,
                )
                .fillMaxWidth(),
            resultMessage = resultMessage
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.1f)
        )

        Text(
            modifier = Modifier.padding(
                horizontal = PaddingMedium,
                vertical = PaddingLarge
            ),
            fontSize = 14.sp,
            text = host
        )
    }
}

@Composable
@Preview
fun MainScreenContentPreview() {
    HomeScreenContent(
        currentUsername = "McQuade",
        resultMessage = SuccessResultMessage(message = "Success"),
        host = "myinstance.mauth.nevis.cloud",
        onUsernameChange = {},
        onRegisterButtonClicked = {},
        onAuthenticateButtonClicked = {},
        onAuthenticateUsernamelessButtonClicked = {},
        onAuthenticationOptionsChange = {},
        onRegistrationOptionsChange = {},
        onRegisterViaWebButtonClicked = {},
        onAuthenticateViaWebButtonClicked = {},
    )
}
