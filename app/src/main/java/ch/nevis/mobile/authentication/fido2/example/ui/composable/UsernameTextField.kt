package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.nevis.mobile.authentication.fido2.example.R

/**
 * Composable that renders a single-line text field for username input.
 *
 * @param modifier Modifier to apply to the text field.
 * @param currentUsername The current value of the username field.
 * @param onUsernameChange Callback invoked when the user changes the text.
 */
@Composable
fun UsernameTextField(modifier: Modifier, currentUsername: String, onUsernameChange: (String) -> Unit) {

    TextField(
        value = currentUsername,
        modifier = modifier,
        maxLines = 1,
        onValueChange = onUsernameChange,
        label = { Text(text = stringResource(R.string.composable_username)) }
    )
}

@Composable
@Preview
fun UsernameTextFieldPreview() {
    UsernameTextField(Modifier.fillMaxWidth(), "Walker") {}
}
