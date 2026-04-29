package ch.nevis.mobile.authentication.fido2.example.data.datasource

import ch.nevis.mobile.authentication.fido2.example.data.model.ApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.ApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.AssertionRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AssertionResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.AttestationRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AttestationResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.IntrospectResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.UserEnrollmentRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.UserEnrollmentResponse

/**
 * Data-source abstraction for Nevis Authentication Cloud HTTP API calls.
 *
 * Each method maps directly to one Authentication Cloud REST endpoint and uses
 * the corresponding data-layer DTO models. All methods are suspend functions.
 */
interface AuthCloudDataSource {

    /**
     * Initiates passkey enrollment for a user.
     *
     * Endpoint: `POST api/v1/users/enroll/`
     *
     * @param request Enrollment request containing the username and FIDO2 options.
     * @return [UserEnrollmentResponse] with the enrollment transaction details.
     */
    suspend fun userEnrollment(request: UserEnrollmentRequest): UserEnrollmentResponse

    /**
     * Submits the WebAuthn attestation object after passkey creation.
     *
     * Endpoint: `POST _app/attestation/result/`
     *
     * @param request Attestation data including the CBOR-encoded attestation object and
     *                the Base64URL-encoded client data JSON.
     * @return [AttestationResponse] with the outcome of the attestation verification.
     */
    suspend fun attestation(request: AttestationRequest): AttestationResponse

    /**
     * Initiates a passkey authentication request.
     *
     * Endpoint: `POST api/v1/approval/`
     *
     * @param request Approval request with an optional username and FIDO2 options.
     * @return [ApprovalResponse] containing the WebAuthn `CredentialRequestOptions` challenge.
     */
    suspend fun approval(request: ApprovalRequest): ApprovalResponse

    /**
     * Submits the WebAuthn assertion response after the user authenticates with a passkey.
     *
     * Endpoint: `POST _app/assertion/result/`
     *
     * @param request Assertion data including the authenticator data, signature, and client data JSON.
     * @return [AssertionResponse] with the access token on success.
     */
    suspend fun assertion(request: AssertionRequest): AssertionResponse

    /**
     * Validates a JWT access token against the Authentication Cloud introspection endpoint.
     *
     * Endpoint: `POST api/v1/introspect`
     *
     * @param request The token to validate.
     * @return [IntrospectResponse] with token claims and validity status.
     */
    suspend fun introspect(request: IntrospectRequest): IntrospectResponse
}
