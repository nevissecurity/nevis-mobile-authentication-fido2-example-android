package ch.nevis.mobile.authentication.fido2.example.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

/**
 * Composable that renders a [SingleChoiceSegmentedButtonRow] for selecting one value from a list.
 *
 * @param values The list of option labels to display.
 * @param currentSelectedIndex The initially selected index, or `-1` if nothing is selected.
 * @param onSelected Callback invoked with the index of the newly selected option.
 */
@Composable
fun Options(
    values: List<String>,
    currentSelectedIndex: Int = -1,
    onSelected: (selectedIndex: Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(currentSelectedIndex) }
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        values.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = values.size),
                onClick = {
                    selectedIndex = index
                    onSelected(selectedIndex)
                },
                selected = selectedIndex == index,
                icon = {}
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun OptionsPreview() {
    Options(listOf("Day", "Month", "Year", "Hour")) {}
}

@Preview
@Composable
fun OptionsSelectedPreview() {
    Options(listOf("Day", "Month", "Year", "Hour"), 2) {}
}

@Preview
@Composable
fun OptionsLongPreview() {
    Options(
        listOf(
            "First too long text",
            "Second too long text",
            "Third too long text",
            "Fourth too long text"
        ), 3
    ) {}
}
