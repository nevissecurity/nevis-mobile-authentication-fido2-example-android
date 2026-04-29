package ch.nevis.mobile.authentication.fido2.example

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the FIDO2 example app.
 *
 * Annotated with [@HiltAndroidApp] to trigger Hilt's code generation and initialise
 * the dependency injection graph at application startup.
 */
@HiltAndroidApp
class Fido2ExampleApplication : Application()
