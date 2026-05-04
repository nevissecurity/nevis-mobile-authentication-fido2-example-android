package ch.nevis.mobile.authentication.fido2.example.data.util

import android.content.Context
import android.os.Build
import ch.nevis.mobile.authentication.fido2.example.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Default implementation of [UserFriendlyNameProvider] that composes a name from
 * the Android device model and the current date/time.
 *
 * The resulting name follows the format: `"<Manufacturer> <Model> <datetime>"`,
 * where the manufacturer prefix is omitted when the model name already starts with it.
 * Example: `"Google Pixel 8 2025-01-15 10:30"`.
 */
class UserFriendlyNameProviderImpl @Inject constructor(
    private val context: Context
) : UserFriendlyNameProvider {
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(context.getString(R.string.user_friendly_name_provider_date_time_format))

    override fun get(): String {
        val date = LocalDateTime.now().format(formatter)
        return "${getDeviceName()} $date"
    }

    private fun getDeviceName(): String {
        if (Build.MANUFACTURER == null && Build.MODEL == null) {
            return context.getString(R.string.user_friendly_name_provider_unknown_device_name)
        }

        val manufacturerLabel = if (Build.MANUFACTURER == null ||
            Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())
        ) {
            ""
        } else {
            "${Build.MANUFACTURER}"
        }

        val modelLabel = Build.MODEL ?: ""
        return "$manufacturerLabel $modelLabel".trim()
    }
}
