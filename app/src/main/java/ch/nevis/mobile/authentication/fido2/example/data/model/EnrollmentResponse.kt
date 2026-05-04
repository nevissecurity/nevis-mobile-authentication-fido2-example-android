package ch.nevis.mobile.authentication.fido2.example.data.model

/**
 * DTO representing the attestation response fields extracted from
 * [AuthenticatorAttestationResponse] and forwarded to the Authentication Cloud attestation endpoint.
 *
 * @property attestationObject Base64URL-encoded CBOR-encoded COSE attestation object.
 * @property clientDataJSON Base64URL-encoded JSON containing the challenge and origin.
 */
data class EnrollmentResponse(
    val attestationObject: String,
    val clientDataJSON: String,
)
