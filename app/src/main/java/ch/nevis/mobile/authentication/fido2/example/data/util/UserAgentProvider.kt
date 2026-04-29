package ch.nevis.mobile.authentication.fido2.example.data.util

/**
 * Provides the HTTP `User-Agent` string for network requests.
 *
 * Reads the value from the `http.agent` system property, which is automatically set
 * by the Android HTTP stack.
 */
object UserAgentProvider {
    /**
     * Returns the current HTTP User-Agent string, or `null` if not available.
     */
    fun get(): String? = System.getProperty("http.agent")
}
