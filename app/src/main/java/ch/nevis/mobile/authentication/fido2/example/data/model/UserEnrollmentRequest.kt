package ch.nevis.mobile.authentication.fido2.example.data.model

import ch.nevis.mobile.authentication.fido2.example.data.util.Constants

/**
 * DTO for the `POST api/v1/users/enroll/` request body.
 *
 * The [channel] field is always set to `"fido2"` to identify the registration channel.
 * The [displayName] mirrors [username] as required by the Authentication Cloud API.
 *
 * @property username The username to enrol.
 * @property fido2Options Optional FIDO2 authenticator selection and attestation options.
 */
data class UserEnrollmentRequest(
    val username: CharSequence,
    val fido2Options: Fido2Options? = null,
) {
    val channel = Constants.CHANNEL_FIDO2
    val displayName = username
}
