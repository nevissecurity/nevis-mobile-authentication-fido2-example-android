package ch.nevis.mobile.authentication.fido2.example.ui.model

/**
 * Sealed class hierarchy representing the result message displayed on the home screen.
 *
 * @property title Optional title of the message (e.g. "Success", "Error").
 * @property message The body of the message.
 */
abstract class ResultMessage(
    val title: String = "",
    val message: String = "",
)

/** Represents the absence of any result message (initial/reset state). */
class EmptyResultMessage() : ResultMessage()

/**
 * Represents a successful operation result.
 *
 * @param title The success title, defaults to `"Success"`.
 * @param message The success message body.
 */
class SuccessResultMessage(
    title: String = "Success",
    message: String,
) : ResultMessage(title, message)

/**
 * Represents a failed or erroneous operation result.
 *
 * @param title The error title, defaults to `"Error"`.
 * @param message The error message body.
 */
class ErrorResultMessage(
    title: String = "Error",
    message: String,
) : ResultMessage(title, message)
