package com.google.android.apps.markers.compose

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.dp

@Composable
fun HyperBorder(modifier: Modifier = Modifier) {
    val shadowSizeDp = 2.dp
    Canvas(modifier = modifier) {
//        drawLine(
//            start = Offset(x = 0f, y = 0f),
//            end = Offset(x = size.width, y = size.height),
//            color = Color.Magenta,
//            strokeWidth = 3.dp.toPx()
//        )
        inset(1.dp.toPx(), 1.dp.toPx(), 2.dp.toPx(), 2.dp.toPx()) {
//        let {
            val w = size.width
            val h = size.height
            drawRect(
                color = Color.Black,
                style = Stroke(width = 2.dp.toPx()),
                topLeft = Offset(x = 0f, y = 0f),
                size = Size(
                    width = w - shadowSizeDp.toPx(),
                    height = h - shadowSizeDp.toPx()
                )
            )
            drawPath(
                path = Path().also {
                    it.moveTo(shadowSizeDp.toPx(), h)
                    it.lineTo(w, h)
                    it.lineTo(w, shadowSizeDp.toPx())
                },
                color = Color.Black,
                style = Stroke(
                    width = shadowSizeDp.toPx(),
                    cap = StrokeCap.Butt
                )
            )
        }
    }
}

@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
}