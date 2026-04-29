package ch.nevis.mobile.authentication.fido2.example.data.datasource

import ch.nevis.mobile.authentication.fido2.example.BuildConfig
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
import ch.nevis.mobile.authentication.fido2.example.data.retrofit.ApiInterface
import retrofit2.Retrofit

/**
 * Retrofit-backed implementation of [AuthCloudDataSource].
 *
 * Creates the [ApiInterface] from the provided [Retrofit] instance and delegates
 * all network calls to it. The `Authorization: Bearer` header is injected at call
 * time using [BuildConfig.AccessToken] for endpoints that require it.
 */
class AuthCloudDataSourceImpl(
    retrofit: Retrofit
) : AuthCloudDataSource {
    private val apiInterface = retrofit.create(ApiInterface::class.java)

    override suspend fun userEnrollment(request: UserEnrollmentRequest): UserEnrollmentResponse {
        return apiInterface.userEnrollment(
            mapOf(
                Pair("Authorization", "Bearer ${BuildConfig.AccessToken}"),
                Pair("Content-Type", "application/json;charset=utf-8")
            ),
            request
        )
    }

    override suspend fun attestation(request: AttestationRequest): AttestationResponse {
        return apiInterface.attestation(request)
    }

    override suspend fun approval(request: ApprovalRequest): ApprovalResponse {
        return apiInterface.approval(
            mapOf(
                Pair("Authorization", "Bearer ${BuildConfig.AccessToken}"),
                Pair("Content-Type", "application/json;charset=utf-8")
            ),
            request
        )
    }

    override suspend fun assertion(request: AssertionRequest): AssertionResponse {
        return apiInterface.assertion(request)
    }

    override suspend fun introspect(request: IntrospectRequest): IntrospectResponse {
        return apiInterface.introspect(
            mapOf(Pair("Authorization", "Bearer ${BuildConfig.AccessToken}")),
            request.token
        )
    }
}
