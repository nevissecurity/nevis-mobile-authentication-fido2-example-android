package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.nevis.mobile.authentication.fido2.example.R
import ch.nevis.mobile.authentication.fido2.example.ui.model.ErrorResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.ResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.model.SuccessResultMessage
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingLarge
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingSmall

/**
 * Composable that renders a bordered result message box.
 *
 * The border and text colour are green for success results and red for error results.
 * Nothing is rendered when [resultMessage] has a blank [ResultMessage.message].
 *
 * @param modifier Modifier to apply to the container column.
 * @param resultMessage The message to display.
 */
@Composable
fun ResultMessage(
    modifier: Modifier = Modifier,
    resultMessage: ResultMessage,
) {
    if (resultMessage.message.isNotBlank()) {
        val fontColor = when (resultMessage) {
            is ErrorResultMessage -> colorResource(R.color.result_error)
            else -> colorResource(R.color.result_success)
        }

        Column(
            modifier = modifier
                .border(
                    1.dp,
                    fontColor,
                    RoundedCornerShape(8.dp)
                )
        ) {
            if (resultMessage.title.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .padding(PaddingLarge, PaddingSmall)
                        .fillMaxWidth(),
                    color = fontColor,
                    text = resultMessage.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                modifier = Modifier
                    .padding(PaddingLarge, PaddingSmall)
                    .fillMaxWidth(),
                color = fontColor,
                text = resultMessage.message,
                fontSize = 12.sp,
                lineHeight = 14.sp,
            )
        }
    }
}

@Preview
@Composable
fun SuccessResultMessagePreview() {
    ResultMessage(resultMessage = SuccessResultMessage(message = "Some message"))
}

@Preview
@Composable
fun ErrorResultMessagePreview() {
    ResultMessage(resultMessage = ErrorResultMessage(message = "Error message"))
}

@Preview
@Composable
fun SuccessResultMessageWithTitlePreview() {
    ResultMessage(resultMessage = SuccessResultMessage(title = "Success", message = "Some message"))
}

@Preview
@Composable
fun ErrorResultMessageWithTitlePreview() {
    ResultMessage(resultMessage = ErrorResultMessage(title = "Error", message = "Error message"))
}

@Preview
@Composable
fun SuccessResultMessageWithLongMessagePreview() {
    ResultMessage(resultMessage = SuccessResultMessage(message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus quis nisi ut justo mattis faucibus a elementum risus. Cras ut libero vel metus ornare mattis. Etiam semper elementum erat, in bibendum lacus mollis semper. In vel auctor velit. Nullam vitae ligula mollis, tristique nisl a, elementum enim. Donec vel blandit arcu. Ut a ligula ut massa consequat rhoncus. Donec consectetur orci nec elit luctus, non bibendum dui dignissim. Cras faucibus eros sollicitudin odio luctus lacinia. Aliquam feugiat est ullamcorper justo aliquam, sit amet accumsan urna varius."))
}
