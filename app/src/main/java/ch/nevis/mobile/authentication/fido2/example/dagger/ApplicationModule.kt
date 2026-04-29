package ch.nevis.mobile.authentication.fido2.example.dagger

import android.content.Context
import androidx.credentials.CredentialManager
import ch.nevis.mobile.authentication.fido2.example.BuildConfig
import ch.nevis.mobile.authentication.fido2.example.data.datasource.AuthCloudDataSource
import ch.nevis.mobile.authentication.fido2.example.data.datasource.AuthCloudDataSourceImpl
import ch.nevis.mobile.authentication.fido2.example.data.repository.Fido2RepositoryImpl
import ch.nevis.mobile.authentication.fido2.example.data.util.UserFriendlyNameProvider
import ch.nevis.mobile.authentication.fido2.example.data.util.UserFriendlyNameProviderImpl
import ch.nevis.mobile.authentication.fido2.example.domain.repository.Fido2Repository
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.AuthenticationUseCase
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.AuthenticationUseCaseImpl
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.IntrospectUseCase
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.IntrospectUseCaseImpl
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.RegistrationUseCase
import ch.nevis.mobile.authentication.fido2.example.domain.usecase.RegistrationUseCaseImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Hilt module that provides application-level singleton dependencies.
 *
 * All bindings are installed into [SingletonComponent] so that a single instance is
 * shared across the entire application lifetime.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    /**
     * Provides [CredentialManager] for creating and retrieving passkeys via the
     * Android Credential Manager API.
     */
    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)

    /**
     * Provides a [Retrofit] instance configured with the Authentication Cloud base URL and
     * a Gson converter factory.
     *
     * The OkHttp client is shared across all Retrofit calls to reuse the underlying connection
     * pool and thread pool, improving network performance.
     *
     * An [HttpLoggingInterceptor] set to [HttpLoggingInterceptor.Level.BODY] is added to log
     * full request and response bodies — useful for development and debugging.
     */
    @Provides
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder = client.newBuilder().addInterceptor(interceptor)

        return Retrofit.Builder().baseUrl(BuildConfig.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

    /**
     * Provides a [Gson] instance used for JSON serialization and deserialization.
     */
    @Provides
    fun provideGson(): Gson = Gson()

    /**
     * Provides a [UserFriendlyNameProvider] that generates device-based authenticator labels.
     */
    @Provides
    fun provideUserFriendlyNameProvider(@ApplicationContext context: Context): UserFriendlyNameProvider =
        UserFriendlyNameProviderImpl(context)

    /**
     * Provides [RegistrationUseCase] for driving the passkey registration ceremony.
     */
    @Provides
    fun provideRegistrationUseCase(
        credentialManager: CredentialManager,
        fido2Repository: Fido2Repository,
        gson: Gson,
        userFriendlyNameProvider: UserFriendlyNameProvider,
    ): RegistrationUseCase =
        RegistrationUseCaseImpl(credentialManager, fido2Repository, gson, userFriendlyNameProvider)

    /**
     * Provides [AuthenticationUseCase] for driving the passkey authentication ceremony.
     */
    @Provides
    fun provideAuthenticationUseCase(
        credentialManager: CredentialManager,
        fido2Repository: Fido2Repository,
        gson: Gson,
    ): AuthenticationUseCase = AuthenticationUseCaseImpl(credentialManager, fido2Repository, gson)

    /**
     * Provides [IntrospectUseCase] for validating JWT access tokens.
     */
    @Provides
    fun provideValidateTokenUseCase(fido2Repository: Fido2Repository): IntrospectUseCase =
        IntrospectUseCaseImpl(fido2Repository)

    /**
     * Provides [Fido2Repository] backed by [Fido2RepositoryImpl].
     */
    @Provides
    fun provideFido2Repository(authCloudDataSource: AuthCloudDataSource): Fido2Repository =
        Fido2RepositoryImpl(authCloudDataSource)

    /**
     * Provides [AuthCloudDataSource] backed by [AuthCloudDataSourceImpl].
     */
    @Provides
    fun provideAuthCloudDataSource(retrofit: Retrofit): AuthCloudDataSource =
        AuthCloudDataSourceImpl(retrofit)
}
