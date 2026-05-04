package ch.nevis.mobile.authentication.fido2.example.domain.repository

import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteRegistrationResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationResponse

/**
 * Domain-layer repository abstraction for FIDO2 passkey operations against the
 * Nevis Authentication Cloud backend.
 *
 * All methods are suspend functions and must be called from a coroutine context.
 * Implementations are expected to translate backend error responses into
 * appropriate exceptions.
 */
interface Fido2Repository {

    /**
     * Initiates a passkey registration ceremony by requesting enrollment options from the server.
     *
     * @param request The username and FIDO2 options for the registration.
     * @return A [StartRegistrationResponse] containing the WebAuthn [CredentialCreationOptions]
     *         challenge and a status token needed for the second phase.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun startRegistration(request: StartRegistrationRequest): StartRegistrationResponse

    /**
     * Completes the passkey registration ceremony by submitting the attestation response to the server.
     *
     * @param request The attestation object, client data JSON, status token, and user-friendly device name.
     * @return A [CompleteRegistrationResponse] acknowledging successful registration.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun completeRegistration(request: CompleteRegistrationRequest): CompleteRegistrationResponse

    /**
     * Initiates a passkey authentication ceremony by requesting assertion options from the server.
     *
     * @param request The optional username (pass `null` for discoverable-credential / usernameless
     *                flow) and FIDO2 options.
     * @return A [StartApprovalResponse] containing the WebAuthn [CredentialRequestOptions] challenge
     *         and a status token needed for the second phase.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun startApproval(request: StartApprovalRequest): StartApprovalResponse

    /**
     * Completes the passkey authentication ceremony by submitting the assertion response to the server.
     *
     * @param request The assertion response and the status token returned by [startApproval].
     * @return A [CompleteApprovalResponse] acknowledging successful authentication.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun completeApproval(request: CompleteApprovalRequest): CompleteApprovalResponse

    /**
     * Validates a JWT access token against the Authentication Cloud introspection endpoint.
     *
     * @param request The token to validate.
     * @return An [IntrospectResponse] describing the token's validity and claims.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun introspect(request: IntrospectRequest): IntrospectResponse
}
