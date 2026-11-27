# Nevis Mobile Authentication FIDO2 Example App (Android)

## Project Overview

A native Android (Kotlin, Jetpack Compose) example app that shows how to register and use FIDO2 passkeys against a Nevis Authentication Cloud backend with the Android Credential Manager (`androidx.credentials`). It does **not** use the Nevis Mobile Authentication SDK (that SDK is FIDO UAF based); every passkey operation goes through `CredentialManager.createCredential` / `getCredential`. It is meant as a learning resource and a source of integration code for customer apps. It is not a production-ready app.

The repository contains one module, **`:app`** (package `ch.nevis.mobile.authentication.fido2.example`), with a single screen (`HomeScreen`).

Flows demonstrated:

- Passkey registration for a username (`POST /api/v1/users/enroll/` → `createCredential` with `CreatePublicKeyCredentialRequest` → `POST /_app/attestation/result/`)
- Passkey authentication (`POST /api/v1/approval/` → `getCredential` with `GetPublicKeyCredentialOption` → `POST /_app/assertion/result/`)
  - with a username (the server returns `allowCredentials`)
  - usernameless, with discoverable credentials
- Browser-based sign-in in a Chrome Custom Tab (registration or authentication on a FIDO2-enabled web page, result returned through a custom URL scheme)
- Token introspection (`POST /api/v1/introspect`) of the JWT returned by the browser-based sign-in
- FIDO2 options selectable in the UI: user verification, authenticator attachment, attestation conveyance and resident key

Passkey auto-fill in the keyboard is **not** implemented: the Credential Manager auto-fill integration needs `View`-based text fields, and the app uses Compose.

Paradigm: **Clean Architecture with MVVM**, wired up with Dagger Hilt. Async work uses Kotlin coroutines:

- `HomeScreenViewModel` (`@HiltViewModel`, `StateFlow` state) launches a use case in `viewModelScope` for each button.
- `RegistrationUseCaseImpl` / `AuthenticationUseCaseImpl` run the whole ceremony as one `suspend` call: get the WebAuthn options from `Fido2Repository`, pass them as JSON to `CredentialManager`, parse the response JSON and send it back through the repository.
- `Fido2RepositoryImpl` maps between domain models and the Retrofit DTOs of `AuthCloudDataSource`.
- The view model shows the outcome (or the error) in a result message.

The example covers the native passkey flows against Authentication Cloud only. For the backend side, the REST API and passkey concepts, use the documentation at https://docs.nevis.net/authcloud. Don't infer Authentication Cloud API behavior from memory.

## Platform APIs & Related Resources

How passkeys are implemented:

- **Android Credential Manager** (`androidx.credentials:credentials` and `credentials-play-services-auth`, the latter needed for Android 13 and lower), no Nevis SDK. The server's `CredentialCreationOptions` / `CredentialRequestOptions` are serialized with Gson and passed unchanged as `requestJson`; the `registrationResponseJson` / `authenticationResponseJson` results are parsed back with Gson (`credentials/model/`).
- The relying party ID comes from the server response (`rp.id` on registration, `rpId` on authentication). The relying party domain must serve a Digital Asset Links file (`/.well-known/assetlinks.json`) that lists the app's package name and the SHA-256 fingerprint of its signing certificate.
- Other dependencies (versions in `gradle/libs.versions.toml`): Jetpack Compose (Material 3, BOM), Navigation Compose, Hilt (with kapt), Retrofit + Gson converter + OkHttp logging interceptor, AndroidX Browser (Custom Tabs), kotlinx.serialization (only for the type-safe navigation route).

Documentation (https://docs.nevis.net):

- [FIDO2/Passkey example apps](https://docs.nevis.net/authcloud/example-apps/fido2-mobile-example-apps) (setup, demo scenarios, production architecture)
- [Access key](https://docs.nevis.net/authcloud/getting-started/access-key), [Test & Debug page](https://docs.nevis.net/authcloud/getting-started/test-access-app)
- Passkeys: [overview](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys), [prerequisites](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/FIDO2-prerequisites), [architecture overview](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/architecture-overview), [browsers and authenticators](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/browsers-and-authenticators), [native app association](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/native-app-association)
- Instance settings: [relying origin and relying party ID](https://docs.nevis.net/authcloud/management-console/system#relying-origin)
- [FIDO2 API use cases](https://docs.nevis.net/authcloud/api-doc/api-use-cases/fido2), indirect communication (recommended for native apps): [register](https://docs.nevis.net/authcloud/api-doc/api-use-cases/fido2/indirect-communication/register-fido2-authenticator), [authenticate](https://docs.nevis.net/authcloud/api-doc/api-use-cases/fido2/indirect-communication/authenticate-with-fido2)
- API reference: [registration](https://docs.nevis.net/authcloud/api-doc/api-reference/registration-ref), [attestation](https://docs.nevis.net/authcloud/api-doc/api-reference/attestation-ref), [approval](https://docs.nevis.net/authcloud/api-doc/api-reference/approval-ref), [assertion](https://docs.nevis.net/authcloud/api-doc/api-reference/assertion-ref), [introspect](https://docs.nevis.net/authcloud/api-doc/api-reference/introspect-ref)
- [REST API overview](https://docs.nevis.net/authcloud/api-doc), [configuration options](https://docs.nevis.net/authcloud/api-doc/config-options), [UX guidelines](https://docs.nevis.net/authcloud/api-doc/ux-guidelines)
- [Authentication Cloud documentation](https://docs.nevis.net/authcloud/)

## Prerequisites & Configuration

Backend: a **Nevis Authentication Cloud** instance and an [access key](https://docs.nevis.net/authcloud/getting-started/access-key) for it. For native passkey operations against the default relying party domain (`<instance>.mauth.nevis.cloud`), mobile passkey support must be enabled on the instance, so that its `assetlinks.json` lists the app with the fingerprint of the certificate you sign with (contact Nevis support). See [native app association](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/native-app-association).

All configuration is **build-time only**. To point the app at your own environment:

1. **Backend settings.** Add to `local.properties` in the project root (git-ignored):
   ```properties
   HOST_NAME=<instance>.mauth.nevis.cloud
   BACKEND_ACCESS_TOKEN=<access key>
   ```
   - `getConfig()` at the bottom of `app/build.gradle.kts` looks the names up in `local.properties`, then in environment variables, system properties and Gradle project properties. If a value is missing, Gradle configuration fails with `GradleException: Getting configuration with name … failed`, so no task runs.
   - The values become `BuildConfig.BaseUrl` (`https://<HOST_NAME>`, so `HOST_NAME` has no scheme) and `BuildConfig.AccessToken`, for every build type. The access key is sent as a bearer token on the enroll, approval and introspect requests and as the `token` query parameter of the web sign-in URL.
2. **Asset statements.** In `app/src/main/res/values/strings.xml`, replace `myinstance.mauth.nevis.cloud` in `asset_statements` with your host (referenced by the `asset_statements` meta-data in `AndroidManifest.xml`).
3. **Signing.** All build types use the `debug` signing config, so by default the APK is signed with the local debug keystore of the machine that builds it. The relying party's `assetlinks.json` must list the SHA-256 fingerprint of the certificate you actually sign with. To sign with a shared keystore, uncomment the `storeFile` / `storePassword` / `keyAlias` / `keyPassword` lines in `app/build.gradle.kts` and set `KEYSTORE_FILE`, `KEYSTORE_PASSWORD`, `KEY_ALIAS` and `KEY_PASSWORD` (resolved by `getConfig()` like the backend settings).
4. **Relying origin.** Authentication Cloud must accept the origin that Credential Manager writes into `clientDataJSON` for the app (`android:apk-key-hash:<hash of the signing certificate>`); configure it as a [relying origin](https://docs.nevis.net/authcloud/management-console/system#relying-origin) of the instance. The debug log line `RegistrationResponseJson: …` (tag `RegistrationUseCaseImpl`) contains the Base64URL-encoded `clientDataJSON`, whose `origin` field shows the exact value.
5. **Web sign-in callback scheme.** `auth_redirect_scheme` in `strings.xml` (`fido2.example.app`) is used both by the `VIEW` intent filter of `MainActivity` and as the `redirectTo` parameter of the web sign-in URL, so change it in that one place. The page must redirect to `<scheme>://success?token=<jwt>` or `<scheme>://failure?error=<message>`. The page itself is hard-coded as `${BuildConfig.BaseUrl}/fido2-test.html` (the Test & Debug page) in `HomeScreen.kt`.
6. **Application ID.** `ch.nevis.mobile.authentication.fido2.example` (`applicationId` and `namespace` in `app/build.gradle.kts`). If you change it, the `package_name` in `assetlinks.json` and the relying origin must follow.
7. **FIDO2 options.** Registration defaults to user verification `required`, attachment `platform`, attestation *unspecified* and resident key `required`; authentication defaults to user verification `required` (`domain/model/Options.kt`). *Unspecified* values are mapped to `null` and left out of the request (Gson skips `null` fields), so the server-side defaults apply (see [configuration options](https://docs.nevis.net/authcloud/api-doc/config-options)). The exception is `authenticatorSelection.requireResidentKey`, which is always sent (`true` for `required` / `preferred`, otherwise `false`).

## Build & Run Commands

`README.md` has the full setup. Quick reference:

```bash
./gradlew assembleDebug        # build (the same build the pull request workflow runs)
./gradlew installDebug         # build and install on a connected device / running emulator

# Lint the fastlane Ruby files:
bundle install && bundle exec rubocop -c .rubocop.yml
```

Requirements: JDK 17 and the Gradle wrapper in the repo (Gradle 8.11.1, AGP 8.10.1, Kotlin 2.1.21). `minSdk` 28 (Android 9), `compileSdk` / `targetSdk` 35, Java 11 bytecode. Passkeys need a physical device, or an emulator with Google Play Services and a signed-in Google account, with a screen lock set. There is no Kotlin linter (ktlint / detekt) configured and there are no unit or instrumentation tests; to verify changes, run the app against an Authentication Cloud instance.

## Architecture

### Source layout (`app/src/main/java/ch/nevis/mobile/authentication/fido2/example/`)

```
Fido2ExampleApplication        @HiltAndroidApp
MainActivity                   @AndroidEntryPoint, singleTask; sets up Compose and opens Custom Tabs
dagger/                        ApplicationModule: CredentialManager, Retrofit/OkHttp, Gson, use cases, repository, data source
ui/
  screen/home/                 HomeScreen (intent handling, web sign-in URLs), HomeScreenViewModel
  composable/                  NavigationComponent, HomeScreenContent, Registration/AuthenticationOptions, UsernameTextField, …
  model/                       ResultMessage, WebAuthorizationResult
  theme/                       Compose theme
domain/
  usecase/                     Registration, Authentication, Introspect (interface + Impl)
  repository/                  Fido2Repository interface
  model/                       Start/Complete Registration and Approval requests/responses, Options (UI option enums), IntrospectResponse
data/
  repository/                  Fido2RepositoryImpl
  datasource/                  AuthCloudDataSource(+Impl): adds the bearer token
  retrofit/                    ApiInterface (endpoint paths)
  model/                       Gson DTOs: UserEnrollment, Approval, Attestation, Assertion, Introspect, CredentialCreation/RequestOptions, Fido2Options, …
  util/                        Constants (channel "fido2"), UserAgentProvider, UserFriendlyNameProvider(+Impl)
credentials/model/             Gson models of the Credential Manager response JSON (PublicKeyCredentialRegistration/AuthenticationResponse, …)
```

Resources: `res/values/strings.xml` (asset statements, redirect scheme, operation names), `AndroidManifest.xml` (intent filter, asset statements meta-data).

### How a passkey ceremony runs

1. `HomeScreenViewModel.register(context)` / `authenticate(context)` / `authenticateUsernameless(context)` sets the loading state and calls the use case with the current username and options. The `context` is the Activity from `LocalContext.current`; Credential Manager needs it to show its UI.
2. The use case calls `Fido2Repository.startRegistration` / `startApproval`. `Fido2Options.from(…)` turns the UI options into the `fido2Options` of the request; the response carries the WebAuthn options and the `statusToken`.
3. The options are serialized with Gson as `requestJson` of a `CreatePublicKeyCredentialRequest` (`preferImmediatelyAvailableCredentials = true`) or of a `GetPublicKeyCredentialOption`, and `CredentialManager` shows the system UI.
4. The response JSON is parsed into `PublicKeyCredentialRegistrationResponse` / `PublicKeyCredentialAuthenticationResponse`, and its Base64URL fields are posted unchanged with the `statusToken` to `_app/attestation/result/` (with a friendly device name from `UserFriendlyNameProvider`) or `_app/assertion/result/`.
5. When the use case returns without an exception, the view model shows "Successful registration." / "Successful authentication." Exceptions (Retrofit `HttpException`, Credential Manager exceptions, …) are shown as an error message; for an `HttpException` the raw error body is shown.

Other details:

- **Web sign-in**: the buttons open `${BaseUrl}/fido2-test.html?token=<access key>&operation=reg|auth&redirectTo=<scheme>` in a Custom Tab (`MainActivity.openCustomTab`). The redirect starts `MainActivity` again (`singleTask`); `HomeScreen` passes the intent to `HomeScreenViewModel.processIntent` (from `ON_CREATE` for a cold start, from an `OnNewIntentListener` otherwise) and clears `intent.data`. A `success` redirect is introspected and shown; a `failure` redirect shows the `error` parameter.
- **Native results are not introspected**: `completeRegistration` / `completeApproval` ignore the attestation / assertion response (including its `token`); only the web sign-in token goes through `IntrospectUseCase`.
- **Scopes**: every `@Provides` in `ApplicationModule` is unscoped, so each injection gets a new instance (including Retrofit and Gson).
- **Errors**: there is no domain error type; exceptions propagate from Retrofit and Credential Manager to `HomeScreenViewModel.handleFailure`.

## Integration Guidance

When moving code into your own app:

| If you want to…                          | Look at                                                                                                                           | Notes                                                                                                                                                                                                                                                                                                                                                                 |
|------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Talk to Authentication Cloud             | `ApiInterface`, `AuthCloudDataSourceImpl`, `Fido2RepositoryImpl`                                                                  | **Don't call the API from the app in production.** Your backend holds the access key and relays the options and results ([indirect communication](https://docs.nevis.net/authcloud/api-doc/api-use-cases/fido2/indirect-communication/register-fido2-authenticator)). Keep the `Fido2Repository` boundary and replace the data source with a client for your backend. |
| Create a passkey                         | `RegistrationUseCaseImpl.createPasskey`                                                                                           | The server options go to `CreatePublicKeyCredentialRequest` as JSON; no conversion is needed.                                                                                                                                                                                                                                                                         |
| Authenticate with or without a username  | `AuthenticationUseCaseImpl.getPasskey`                                                                                            | Without a username the server sends no `allowCredentials`, so every discoverable credential for the relying party is offered.                                                                                                                                                                                                                                         |
| Send the credential back                 | `Fido2RepositoryImpl.completeRegistration` / `completeApproval`, `credentials/model/`                                             | The response JSON fields are already Base64URL encoded; post them as they are, plus the `statusToken`.                                                                                                                                                                                                                                                                |
| Map UI options to the request            | `domain/model/Options.kt`, `data/model/Fido2Options.kt`                                                                           | Leave unset options out so the server defaults apply.                                                                                                                                                                                                                                                                                                                 |
| Sign in through a web page               | `HomeScreen` (URL), `MainActivity.openCustomTab`, `HomeScreenViewModel.processIntent`, the intent filter in `AndroidManifest.xml` | Use Custom Tabs, not `WebView` (no WebAuthn support). Point it at your own FIDO2 login page; don't put the access key in the URL.                                                                                                                                                                                                                                     |
| Validate the resulting token             | `IntrospectUseCaseImpl`, `Fido2RepositoryImpl.introspect`                                                                         | Introspection needs the access key, so it belongs on your backend.                                                                                                                                                                                                                                                                                                    |
| Associate the app with the relying party | signing config in `app/build.gradle.kts`, `asset_statements`                                                                      | [Native app association](https://docs.nevis.net/authcloud/authentication-methods/Trusted-key-passkeys/native-app-association); the fingerprint differs between debug, release and Play App Signing keys.                                                                                                                                                              |
| Handle errors                            | `HomeScreenViewModel.handleFailure`                                                                                               | Handle the Credential Manager cancellation exceptions separately from real failures.                                                                                                                                                                                                                                                                                  |

Demo-only code that should not be copied as is:

- the access key compiled into `BuildConfig` (so it is in the APK), sent from the device as a bearer token and as a query parameter of the web sign-in URL (see the production architecture on the [example apps page](https://docs.nevis.net/authcloud/example-apps/fido2-mobile-example-apps))
- calling the attestation, assertion and introspect endpoints directly from the app
- `HttpLoggingInterceptor` at level `BODY`, which logs headers (including `Authorization`) and bodies, and the `Log.d` of the credential response JSON
- showing raw error bodies and token claims to users
- the generated device name (`UserFriendlyNameProvider`) used as the authenticator's friendly name
- the FIDO2 option pickers, which exist to explore server behavior
- signing every build type with the debug signing config, and the release build without minification
- the UI components and styling (`ui/composable/`, `ui/theme/`)
- the placeholder URL scheme, the `myinstance` asset statements and the Test & Debug page as sign-in page

If something isn't covered here, check https://docs.nevis.net/authcloud (API use cases and API reference) before you write code. Don't guess the API.

## Conventions & Gotchas

- **Signing certificate must match**: a passkey request fails if the relying party's `assetlinks.json` doesn't list the package name and the fingerprint of the certificate the installed APK is signed with, or if the app's `android:apk-key-hash:` origin isn't accepted by the instance. Every machine has its own debug keystore, so a debug build from another machine has a different fingerprint and origin.
- **Cancellation is shown as an error**: only `kotlinx.coroutines.CancellationException` is rethrown; Credential Manager's `CreateCredentialCancellationException` / `GetCredentialCancellationException` end up in `handleFailure` like any other failure.
- **Silent success paths**: `AuthenticationUseCaseImpl` does nothing if the returned credential is not a `PublicKeyCredential`, and the attestation / assertion responses are not checked beyond the HTTP status (Retrofit throws `HttpException` only for non-2xx). In both cases the view model reports success.
- **Malformed redirects crash the app**: `processIntent` throws `IllegalStateException` if a `success` redirect has no `token` or a `failure` redirect has no `error`, and nothing catches it.
- **Keep clearing `intent.data`** after handling the redirect; otherwise a configuration change or a new `ON_CREATE` processes the same redirect again.
- **Endpoint paths**: the paths in `ApiInterface` are relative to `BuildConfig.BaseUrl` and, except `api/v1/introspect`, end with `/`. Keep them as they are when you add endpoints next to them.
- **JSON**: Gson is used for Retrofit and for the Credential Manager JSON, and DTO property names are the WebAuthn / API field names. kotlinx.serialization is only used for the navigation route (`Home`).
- **Code style**: Kotlin official code style (`kotlin.code.style=official`), `.editorconfig` (4 spaces, LF, final newline). Document classes and public members with KDoc.
- **Never commit secrets**: `local.properties`, keystores (`*.jks`) and fastlane `.env` files are git-ignored and must stay that way. Don't commit a real host name or access key in `strings.xml` or elsewhere.
