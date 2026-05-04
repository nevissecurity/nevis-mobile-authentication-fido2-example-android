package ch.nevis.mobile.authentication.fido2.example.domain.model

/**
 * Domain model representing the input for the first phase of the passkey authentication ceremony.
 *
 * @property username The username for account-bound authentication. Pass `null` to initiate a
 *                    discoverable-credential (usernameless) flow, where the device's passkey list
 *                    is presented without a username hint.
 * @property authenticationOptions FIDO2 options for the authentication ceremony.
 */
data class StartApprovalRequest(
    val username: String? = null,
    val authenticationOptions: AuthenticationOptions,
)
