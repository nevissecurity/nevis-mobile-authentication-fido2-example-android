package ch.nevis.mobile.authentication.fido2.example.ui.model

/**
 * Sealed class hierarchy representing the outcome of a web-based OAuth/OIDC authorization
 * flow launched via Chrome Custom Tabs.
 */
abstract class WebAuthorizationResult

/**
 * Indicates a successful web authorization. The server redirected to the app's deep-link
 * with a JWT access token as a query parameter.
 *
 * @property token The JWT access token returned by the authorization server.
 */
data class SuccessWebAuthorizationResult(
    val token: String
) : WebAuthorizationResult()

/**
 * Indicates a failed web authorization. The server redirected to the app's deep-link
 * with an error query parameter.
 *
 * @property error Human-readable error description returned by the server.
 */
data class FailureWebAuthorizationResult(
    val error: String
) : WebAuthorizationResult()
