package ch.nevis.mobile.authentication.fido2.example.data.retrofit

import ch.nevis.mobile.authentication.fido2.example.data.model.ApprovalRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.ApprovalResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.AssertionRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AssertionResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.AttestationRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.AttestationResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.IntrospectResponse
import ch.nevis.mobile.authentication.fido2.example.data.model.UserEnrollmentRequest
import ch.nevis.mobile.authentication.fido2.example.data.model.UserEnrollmentResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Retrofit interface declaring all Nevis Authentication Cloud HTTP endpoints used by this app.
 *
 * Instances are created by [Retrofit.create] and should only be used through [AuthCloudDataSourceImpl].
 */
interface ApiInterface {

    /**
     * `POST api/v1/users/enroll/` — enrols a new user and returns a WebAuthn
     * `CredentialCreationOptions` challenge.
     */
    @POST("api/v1/users/enroll/")
    suspend fun userEnrollment(
        @HeaderMap headers: Map<String, String>,
        @Body userEnrollmentRequest: UserEnrollmentRequest
    ): UserEnrollmentResponse

    /**
     * `POST _app/attestation/result/` — submits a WebAuthn attestation object after passkey
     * creation to complete registration.
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json;charset=utf-8"
    )
    @POST("_app/attestation/result/")
    suspend fun attestation(
        @Body attestationRequest: AttestationRequest
    ): AttestationResponse

    /**
     * `POST api/v1/approval/` — requests a WebAuthn `CredentialRequestOptions` challenge to
     * start the authentication ceremony.
     */
    @POST("api/v1/approval/")
    suspend fun approval(
        @HeaderMap headers: Map<String, String>,
        @Body approvalRequest: ApprovalRequest
    ): ApprovalResponse

    /**
     * `POST _app/assertion/result/` — submits the WebAuthn assertion response after the user
     * authenticates with a passkey.
     */
    @POST("_app/assertion/result/")
    suspend fun assertion(
        @Body assertionRequest: AssertionRequest
    ): AssertionResponse

    /**
     * `POST api/v1/introspect` — validates a JWT access token and returns its claims.
     */
    @FormUrlEncoded
    @POST("api/v1/introspect")
    suspend fun introspect(
        @HeaderMap headers: Map<String, String>,
        @Field("token") token: String
    ): IntrospectResponse
}
