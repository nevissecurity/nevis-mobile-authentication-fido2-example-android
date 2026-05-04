<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://www.nevis.net/hubfs/Nevis%202023%20theme/Icons/negativ.svg">
  <source media="(prefers-color-scheme: light)" srcset="https://www.nevis.net/hubfs/Nevis%202023%20theme/Icons/positiv.svg">
  <img alt="Fallback image description" src="https://www.nevis.net/hubfs/Nevis/images/logotype.svg">
</picture>

# Nevis Mobile Authentication FIDO2 Example App

Android example app demonstrating [FIDO2](https://fidoalliance.org/fido2/) passkey registration and authentication with a [Nevis Authentication Cloud](https://www.nevis.net/en/authentication-cloud) backend using the [Android Credential Manager](https://developer.android.com/identity/sign-in/credential-manager).

[![Main Branch Commit](https://github.com/nevissecurity/nevis-mobile-authentication-fido2-example-android/actions/workflows/main.yml/badge.svg)](https://github.com/nevissecurity/nevis-mobile-authentication-fido2-example-android/actions/workflows/main.yml)
[![Verify Pull Request](https://github.com/nevissecurity/nevis-mobile-authentication-fido2-example-android/actions/workflows/pr.yml/badge.svg)](https://github.com/nevissecurity/nevis-mobile-authentication-fido2-example-android/actions/workflows/pr.yml)

## Features

- **Passkey Registration** — create a [FIDO2 passkey](https://passkeys.dev) bound to the user's Google account via Android Credential Manager
- **Passkey Authentication (username-based)** — authenticate with an existing passkey for a specific username
- **Passkey Authentication (usernameless)** — [discoverable-credential](https://www.w3.org/TR/webauthn/#client-side-discoverable-credential) authentication without providing a username upfront
- **Web-based OAuth/OIDC** — browser-based authorization flow via Chrome Custom Tabs with deep-link callback handling
- **JWT Token Introspection** — validate access tokens returned by the Authentication Cloud server
- **Configurable FIDO2 options** — user verification requirement, authenticator attachment, attestation conveyance preference, resident key requirement

## FIDO2 Options

The app exposes four option groups that are sent to the server as part of the registration or authentication request. See the [WebAuthn specification](https://www.w3.org/TR/webauthn/) for the full definition of each option.

### User Verification
Controls whether the authenticator must verify the user (e.g. biometrics, PIN). See [`userVerification`](https://www.w3.org/TR/webauthn/#dom-publickeycredentialrequestoptions-userverification).

| Value | Meaning |
|---|---|
| `required` | User verification is mandatory |
| `preferred` | User verification is preferred but not mandatory |
| `discouraged` | User verification should not be performed |
| `unspecified` | No preference; the server decides |

### Attestation
Controls whether the server requests cryptographic proof of the authenticator's provenance. See [attestation conveyance preference](https://www.w3.org/TR/webauthn/#enum-attestation-convey).

| Value | Meaning |
|---|---|
| `none` | No attestation required |
| `indirect` | Anonymised attestation statement requested |
| `direct` | Full attestation statement requested |
| `unspecified` | No preference |

### Authenticator Attachment
Restricts which class of authenticator may be used. See [`authenticatorAttachment`](https://www.w3.org/TR/webauthn/#enum-attachment).

| Value | Meaning |
|---|---|
| `platform` | Built-in authenticator (fingerprint sensor, face recognition) |
| `cross-platform` | Roaming authenticator (security key, another device) |
| `unspecified` | No restriction |

### Resident Key
Controls whether the credential is stored on the authenticator (discoverable credential), enabling usernameless authentication. See [`residentKey`](https://www.w3.org/TR/webauthn/#dom-authenticatorselectioncriteria-residentkey).

| Value | Meaning |
|---|---|
| `required` | A resident/discoverable credential must be created |
| `preferred` | A resident credential is preferred |
| `discouraged` | A non-resident credential is preferred |
| `unspecified` | No preference |

## Getting Started

Before you start, ensure you have:
- A running [Nevis Authentication Cloud](https://docs.nevis.net/authcloud/) instance.
- An [access key](https://docs.nevis.net/authcloud/getting-started/access-key) for your Authentication Cloud instance.

Your development setup has to meet the following prerequisites:

| Requirement | Version |
|---|---|
| Android | 9+ (API level 28+) |
| JDK | 17 |
| Gradle | 8.11.1+ |
| Android Studio | Meerkat Feature Drop 2024.3.2 |

## Passkeys Support

The [Android Credential Manager](https://developer.android.com/identity/sign-in/credential-manager) library supports passkeys on Android 9 (API level 28) and above. See the official [Passkeys on Android](https://developer.android.com/training/sign-in/passkeys) guide for a full overview.

| Android Version | API Level | Credential Provider Support |
|---|---|---|
| Android 9–12 | 28–32 | Google Password Manager only |
| Android 13 | 33 | Google Password Manager only |
| Android 14+ | 34+ | Multiple providers supported |

> **Note:** Prefilling the username field via autofill is only available for `View`-based UIs. As this app uses **Jetpack Compose**, automatic credential field pre-filling is not available. See [credential-manager-autofill](https://developer.android.com/identity/autofill/credential-manager-autofill) and [indicate_credential_fields](https://developer.android.com/identity/sign-in/credential-manager#indicate_credential_fields).

## Configuration

Create a `local.properties` file in the project root (it is git-ignored by default) and add:

```properties
HOST_NAME=<your-authentication-cloud-hostname>
BACKEND_ACCESS_TOKEN=<your-access-token>
```

> **CI environments:** Instead of `local.properties`, the build also accepts `HOST_NAME` and `BACKEND_ACCESS_TOKEN` as environment variables (or system/project properties). This is what the GitHub Actions workflows use.

The values are injected at build time as `BuildConfig` fields and are never hardcoded in source.

You must also sign your application with a certificate whose SHA256 fingerprints are published in the `assetlinks.json` exposed in your AuthCloud instance (URL `https://<HOST_NAME>/./well-known/assetlinks.json`). To sign your application, you must provide the following properties to `local.properties`:

- `KEYSTORE_FILE`: path to the key store file.
- `KEYSTORE_PASSWORD`: the password to access the key store.
- `KEY_ALIAS`: the alias of the key to be used to sign.
- `KEY_PASSWORD`: the password to access the key.

You must also uncomment the lines in `app/build.gradle.kts` where the `signingConfig` is defined.

An example of `assetlinks.json` can be found below. Note that you must also include the package_name of this application (`ch.nevis.mobile.authentication.fido2.example`).

```properties title="./well-known/assetlinks.json"
[{"relation":["delegate_permission/common.handle_all_urls"],"target":{"namespace":"android_app","package_name":"ch.nevis.mobile.authentication.fido2.example","sha256_cert_fingerprints":["CF:B5:E4:C3:AD:9D:02:7F:C1:B1:C9:0E:43:D6:B7:38:C8:66:97:08:73:76:BD:95:91:CB:30:20:8C:89:80:91","91:D7:03:AF:EB:09:57:27:40:91:9E:A7:4F:F1:D1:DD:4A:49:15:27:7E:BC:F3:91:5C:BC:30:F0:32:07:57:B7"]}}]
```

You must update the [relying origin](https://docs.nevis.net/authcloud/management-console/system#relying-origin) in the AuthCloud instance configuration. Here you must provide as origin the key hash of your application (of type `android:apk-key-hash:<key hash>`). Refer to [Facet ID](https://docs.nevis.net/nevisaccessapp/appendixes/facetid-calculation#android) documentation to know how to calculate the origin.

## Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/nevissecurity/nevis-mobile-authentication-fido2-example-android.git
   ```
2. Create `local.properties` in the project root with `HOST_NAME` and `BACKEND_ACCESS_TOKEN` (see [Configuration](#configuration)).
3. Open the project in Android Studio and wait for Gradle sync to finish.
4. Connect a physical device or start an emulator running **API 28+**.
5. Run the app via **▶ Run** in Android Studio, or from the command line:
   ```bash
   ./gradlew assembleDebug
   ```

## Architecture

The project follows [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) with three layers:

```
┌─────────────────────────────────────────────────┐
│  Presentation (ui/)                              │
│  Jetpack Compose screens • HomeScreenViewModel   │
│  StateFlow-driven UI state • Navigation          │
├─────────────────────────────────────────────────┤
│  Domain (domain/)                               │
│  Use cases • Repository interface               │
│  Domain models • Business logic                 │
├─────────────────────────────────────────────────┤
│  Data (data/)                                   │
│  Fido2RepositoryImpl • AuthCloudDataSourceImpl  │
│  Retrofit API interface • DTO models            │
└─────────────────────────────────────────────────┘
```

Dependency injection is provided by **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** (`dagger/ApplicationModule`).

## Registration Flow

1. User enters a username and taps **Register**.
2. `HomeScreenViewModel.register()` calls `RegistrationUseCaseImpl.execute()`.
3. The use case calls `fido2Repository.startRegistration()`, which POSTs to `/api/v1/users/enroll/` and receives a [WebAuthn `CredentialCreationOptions`](https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialcreationoptions) challenge.
4. The use case calls [`CredentialManager.createCredential()`](https://developer.android.com/reference/androidx/credentials/CredentialManager), triggering the system passkey creation UI.
5. On success, the [attestation response](https://www.w3.org/TR/webauthn/#sctn-attestation) is sent to `fido2Repository.completeRegistration()` (POST `/_app/attestation/result/`).
6. The result is surfaced to the UI via `HomeScreenViewModel.resultMessage`.

## Authentication Flow

1. User optionally enters a username (leave blank for usernameless) and taps **Authenticate**.
2. `HomeScreenViewModel.authenticate()` calls `AuthenticationUseCaseImpl.execute()`.
3. The use case calls `fido2Repository.startApproval()`, which POSTs to `/api/v1/approval/` with the username (or without for [discoverable credential](https://www.w3.org/TR/webauthn/#client-side-discoverable-credential) flow) and receives a [WebAuthn `CredentialRequestOptions`](https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialrequestoptions) challenge.
4. The use case calls [`CredentialManager.getCredential()`](https://developer.android.com/reference/androidx/credentials/CredentialManager), triggering the system passkey selection UI.
5. On success, the [assertion response](https://www.w3.org/TR/webauthn/#sctn-verifying-assertion) is sent to `fido2Repository.completeApproval()` (POST `/_app/assertion/result/`).
6. The result (including the access token) is surfaced to the UI.

## Dependencies

| Library | Purpose |
|---|---|
| [Jetpack Compose](https://developer.android.com/compose) + Material 3 | Declarative UI framework |
| [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) | Dependency injection |
| [Retrofit 2](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/) | HTTP networking |
| [Android Credential Manager](https://developer.android.com/identity/sign-in/credential-manager) | Passkey creation and authentication |
| [Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose) | In-app navigation |
| [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) | JSON serialization |
| [Chrome Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs) | Web-based authorization flow |

## Troubleshooting

| Symptom | Likely Cause & Fix |
|---|---|
| Build fails with `GradleException: Getting configuration with name HOST_NAME failed` | `HOST_NAME` or `BACKEND_ACCESS_TOKEN` is not set — add them to `local.properties` or export as environment variables before building |
| Passkey creation fails on emulator | Use a physical device, or an emulator with Google Play Services and a Google account signed in |
| "No credentials available" during authentication | Register a passkey first on the same device using the same Google account |

---

© 2026 made with ❤ by Nevis
