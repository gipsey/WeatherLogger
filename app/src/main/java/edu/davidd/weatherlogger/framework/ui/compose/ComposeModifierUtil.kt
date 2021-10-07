package edu.davidd.weatherlogger.framework.ui.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) =
    this.then(
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)

            check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
            val firstBaseline = placeable[FirstBaseline]

            val width = placeable.width
            val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
            val height = placeable.height + placeableY

            layout(width, height) {
                placeable.placeRelative(0, placeableY)
            }
        }
    )