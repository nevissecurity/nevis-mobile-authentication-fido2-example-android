package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse

/**
 * Use case that validates a JWT access token against the Authentication Cloud
 * introspection endpoint.
 */
interface IntrospectUseCase {

    /**
     * Validates the given token and returns its claims.
     *
     * @param request The token to validate.
     * @return An [IntrospectResponse] with the token's validity status and JWT claims.
     * @throws retrofit2.HttpException if the server returns a non-2xx response.
     */
    suspend fun execute(request: IntrospectRequest): IntrospectResponse
}
