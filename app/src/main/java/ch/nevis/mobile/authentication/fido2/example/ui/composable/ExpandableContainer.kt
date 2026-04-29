package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.nevis.mobile.authentication.fido2.example.ui.theme.NoPadding
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingLarge
import ch.nevis.mobile.authentication.fido2.example.ui.theme.PaddingMedium

/**
 * Composable that wraps content in a collapsible [OutlinedCard] with a clickable header.
 *
 * @param modifier Modifier to apply to the card.
 * @param title The header label.
 * @param isExpanded Whether the container is currently expanded.
 * @param onHeaderClicked Callback invoked when the header row is tapped.
 * @param content The composable content shown when [isExpanded] is `true`.
 */
@Composable
fun ExpandableContainer(
    modifier: Modifier = Modifier,
    title: String,
    isExpanded: Boolean = false,
    onHeaderClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .animateContentSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .clickable(enabled = true, onClick = onHeaderClicked),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = PaddingMedium,
                            vertical = PaddingLarge,
                        ),
                    text = title,
                )
                Spacer(
                    modifier = Modifier
                        .weight(0.1f)
                )
                Icon(
                    modifier = Modifier
                        .padding(
                            horizontal = PaddingMedium,
                            vertical = PaddingLarge,
                        )
                        .size(22.dp),
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand icon",
                )
            }
            if (isExpanded) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            start = PaddingMedium,
                            top = PaddingMedium,
                            end = PaddingMedium,
                            bottom = NoPadding,
                        )
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = PaddingMedium,
                            top = PaddingMedium,
                            end = PaddingMedium,
                            bottom = PaddingLarge,
                        )
                        .fillMaxWidth(),
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
@Preview
fun ExpandableContainerCollapsedPreview() {
    ExpandableContainer(title = "Barney Ross") {
        Box(
            modifier = Modifier
                .background(Color.Red)
                .width(100.dp)
                .height(50.dp)
        )
    }
}

@Composable
@Preview
fun ExpandableContainerExpandedPreview() {
    ExpandableContainer(title = "Lee Christmas", isExpanded = true) {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}
