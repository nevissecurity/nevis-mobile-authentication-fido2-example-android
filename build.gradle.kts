// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ktlint) apply true
}

// Workaround for https://github.com/JLLeitschuh/ktlint-gradle/issues/1037:
// the ktlint Gradle plugin only auto-detects the version from ktlint-plugins.properties
// for the root project, not for subprojects, so it needs to be propagated explicitly.
ktlint.version.let { ktlintVersion ->
    subprojects {
        pluginManager.withPlugin("org.jlleitschuh.gradle.ktlint") {
            configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
                version.set(ktlintVersion)
            }
        }
    }
}
