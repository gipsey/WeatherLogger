package edu.davidd.weatherlogger.framework.ui.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

object AppShape {
    val Corner = RoundedCornerShape(16.dp)
    val NoCorner = RoundedCornerShape(0.dp)
}
