package ch.nevis.mobile.authentication.fido2.example.data.model

import ch.nevis.mobile.authentication.fido2.example.data.util.UserAgentProvider

/**
 * DTO for the `POST _app/assertion/result/` request body.
 *
 * Carries the WebAuthn assertion response produced by Android Credential Manager.
 *
 * @property id The Base64URL-encoded credential ID selected by the user.
 * @property response Authenticator data, signature, client data JSON, and optional user handle.
 * @property statusToken The opaque token returned by the approval endpoint.
 * @property type The credential type, typically `"public-key"`.
 * @property userAgent The HTTP User-Agent string, auto-populated from system properties.
 */
data class AssertionRequest(
    val id: String,
    val response: AuthenticationResponse,
    val statusToken: String,
    val type: String,
    val userAgent: String? = UserAgentProvider.get()
)
