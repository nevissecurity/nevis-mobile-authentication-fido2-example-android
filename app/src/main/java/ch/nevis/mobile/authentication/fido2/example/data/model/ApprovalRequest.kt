package ch.nevis.mobile.authentication.fido2.example.data.model

import ch.nevis.mobile.authentication.fido2.example.data.util.Constants

/**
 * DTO for the `POST api/v1/approval/` request body.
 *
 * The [channel] field is always set to `"fido2"`. Leave [username] as `null` to trigger
 * a discoverable-credential (usernameless) authentication flow.
 *
 * @property username The username for account-bound authentication, or `null` for usernameless flow.
 * @property fido2Options FIDO2 options for the authentication ceremony.
 */
data class ApprovalRequest(
    val username: CharSequence? = null,
    val fido2Options: Fido2Options,
) {
    val channel = Constants.CHANNEL_FIDO2
}
