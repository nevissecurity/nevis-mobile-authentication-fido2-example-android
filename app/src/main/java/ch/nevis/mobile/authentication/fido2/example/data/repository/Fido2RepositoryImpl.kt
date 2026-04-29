package ch.nevis.mobile.authentication.fido2.example.data.repository

import ch.nevis.mobile.authentication.fido2.example.data.datasource.AuthCloudDataSource
import ch.nevis.mobile.authentication.fido2.example.data.model.ApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AssertionRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AttestationRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AuthenticationResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.EnrollmentResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.Fido2Options
import ch.nevis.mobile.authentication.fido2.example.data.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.UserEnrollmentRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.toDomain
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.CompleteRegistrationResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.StartRegistrationResponse
import ch.nevis.mobile.authentication.fido2.example.domain.repository.Fido2Repository

/**
 * Implementation of [Fido2Repository] that translates between the domain model layer and the
 * data-layer DTOs used by [AuthCloudDataSource].
 *
 * Every method maps domain request models to the appropriate data-layer request DTOs, delegates
 * the network call to [AuthCloudDataSource], and then maps the returned data-layer DTOs back to
 * domain response models.
 */
class Fido2RepositoryImpl(
    private val authCloudDataSource: AuthCloudDataSource
) : Fido2Repository {
    override suspend fun startRegistration(request: StartRegistrationRequest): StartRegistrationResponse {
        val userEnrollmentResponse =
            authCloudDataSource.userEnrollment(
                UserEnrollmentRequest(
                    request.username,
                    Fido2Options.from(request.registrationOptions)
                )
            )
        return StartRegistrationResponse(
            userEnrollmentResponse.username,
            userEnrollmentResponse.enrollment.statusToken,
            userEnrollmentResponse.enrollment.credentialCreationOptions,
        )
    }

    override suspend fun completeRegistration(request: CompleteRegistrationRequest): CompleteRegistrationResponse {
        authCloudDataSource.attestation(
            AttestationRequest(
                id = request.publicKeyCredentialRegistrationResponse.id,
                response = EnrollmentResponse(
                    request.publicKeyCredentialRegistrationResponse.response.attestationObject,
                    request.publicKeyCredentialRegistrationResponse.response.clientDataJSON,
                ),
                statusToken = request.statusToken,
                type = request.publicKeyCredentialRegistrationResponse.type,
                userFriendlyName = request.userFriendlyName
            )
        )
        return CompleteRegistrationResponse()
    }

    override suspend fun startApproval(request: StartApprovalRequest): StartApprovalResponse {
        val approvalResponse = authCloudDataSource.approval(
            ApprovalRequest(
                request.username,
                Fido2Options.from(request.authenticationOptions),
            )
        )
        return StartApprovalResponse(
            approvalResponse.credentialRequestOptions,
            approvalResponse.statusToken
        )
    }

    override suspend fun completeApproval(request: CompleteApprovalRequest): CompleteApprovalResponse {
        authCloudDataSource.assertion(
            AssertionRequest(
                request.publicKeyCredentialAuthenticationResponse.id,
                AuthenticationResponse(
                    request.publicKeyCredentialAuthenticationResponse.response.authenticatorData,
                    request.publicKeyCredentialAuthenticationResponse.response.clientDataJSON,
                    request.publicKeyCredentialAuthenticationResponse.response.signature,
                    request.publicKeyCredentialAuthenticationResponse.response.userHandle
                ),
                request.statusToken,
                request.publicKeyCredentialAuthenticationResponse.type,
            )
        )
        return CompleteApprovalResponse()
    }

    override suspend fun introspect(request: ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectRequest): IntrospectResponse {
        val introspectResponse = authCloudDataSource.introspect(IntrospectRequest(request.token))
        return introspectResponse.toDomain()
    }
}
