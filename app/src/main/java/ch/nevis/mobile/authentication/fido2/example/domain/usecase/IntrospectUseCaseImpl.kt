package ch.nevis.mobile.authentication.fido2.example.domain.usecase

import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectRequest
import ch.nevis.mobile.authentication.fido2.example.domain.model.IntrospectResponse
import ch.nevis.mobile.authentication.fido2.example.domain.repository.Fido2Repository

/**
 * Default implementation of [IntrospectUseCase] that delegates token validation
 * to [Fido2Repository].
 */
class IntrospectUseCaseImpl(
    private val fido2Repository: Fido2Repository,
) : IntrospectUseCase {

    /**
     * Delegates introspection to [Fido2Repository.introspect] and returns the result.
     *
     * @param request The token to validate.
     * @return The [IntrospectResponse] with validity status and JWT claims.
     */
    override suspend fun execute(request: IntrospectRequest): IntrospectResponse {
        return fido2Repository.introspect(request)
    }
}
