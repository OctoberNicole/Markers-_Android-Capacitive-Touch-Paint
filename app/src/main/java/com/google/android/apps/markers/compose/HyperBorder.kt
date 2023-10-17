package com.google.android.apps.markers.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Outline.Rectangle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

fun Modifier.hyperBorder_old(width: Dp = 2.dp, depth: Dp = 2.dp) = this
    .then(
        drawBehind {
            val w = size.width
            val h = size.height
            val shadowPx = depth.toPx()
            val widthPx = width.toPx()
            drawPath(
                path = Path.combine(PathOperation.Difference,
                    Path().also {
                        it.addRect(Rect(0f, 0f, w - shadowPx, h - shadowPx))
                        it.addRect(Rect(shadowPx, shadowPx, w, h))
                    },
                    Path().also {
                        it.addRect(
                            Rect(
                                widthPx, widthPx,
                                w - widthPx - shadowPx, h - widthPx - shadowPx
                            )
                        )
                    }),
                color = Color.Green
            )
        }
    )
    .then(
        padding(
            start = width, top = width,
            end = width + depth, bottom = width + depth
        )
    )

enum class HyperStyle {
    Pixel,
    Rounded
}

fun Modifier.hyperBorder(width: Dp = 2.dp,
                         cornerRadius: Dp = -(1.dp),
                         depth: Dp = 2.dp,
                         color: Color = Color.Black,
                         style: HyperStyle = HyperStyle.Pixel) = this
    .then(
        drawWithContent {
            drawContent()

            val w = size.width
            val h = size.height
            val shadowPx = depth.toPx()
            val widthPx = width.toPx()
            val cornerPx = cornerRadius
                .toPx()
                .let { if (it >= 0) it else widthPx }
            val contentRect = Rect(widthPx, widthPx, w - widthPx - shadowPx, h - widthPx - shadowPx)
            drawPath(
                path = Path.combine(PathOperation.Difference,
                    Path().also {
                        when (style) {
                            HyperStyle.Pixel -> {
                                val frameRect = Rect(0f, 0f, w - shadowPx, h - shadowPx)
                                val shadowRect = Rect(shadowPx, shadowPx, w, h)
                                it.addRect(frameRect)
                                it.addRect(shadowRect)
                            }

                            HyperStyle.Rounded -> {
                                val fullRect = Rect(0f, 0f, w, h)
                                it.addRoundRect(RoundRect(fullRect, cornerPx, cornerPx))
                            }
                        }
                    },
                    Path().also {
                        when (style) {
                            HyperStyle.Pixel -> it.addRect(contentRect)
                            HyperStyle.Rounded -> it.addRoundRect(
                                RoundRect(
                                    contentRect,
                                    cornerPx - widthPx,
                                    cornerPx - widthPx
                                )
                            )
                        }
                    }),
                color = color
            )
        }
    )
    .then(
        padding(
            start = width, top = width,
            end = width + depth, bottom = width + depth
        )
    )

@Preview(
    widthDp = 500,
    heightDp = 300,
)
@Composable
fun HyperBorderPreview_Rounded() {
    Box(Modifier.background(Color.Red)) {
        Box(
            Modifier
                .fillMaxSize()
                .hyperBorder(
                    width = 20.dp,
                    cornerRadius = 60.dp,
                    depth = 60.dp,
                    style = HyperStyle.Rounded
                )
                .background(Color(0x80FF00FF))
        ) {
            Canvas(Modifier.fillMaxSize()) {
                drawLine(color = Color.Blue,
                    strokeWidth = 10.dp.toPx(),
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height))
                drawLine(color = Color.Blue,
                    strokeWidth = 10.dp.toPx(),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height))
                drawRect(color = Color.Blue,
                    style = Stroke(width = 10.dp.toPx()),
                    size = this.size)
            }
        }
    }
}

@Preview(
    widthDp = 500,
    heightDp = 300,
)
@Composable
fun HyperBorderPreview_Pixel() {
    Box(Modifier.background(Color.Red)) {
        Box(
            Modifier
                .fillMaxSize()
                .hyperBorder(width = 20.dp, depth = 60.dp, style = HyperStyle.Pixel)
                .background(Color(0x80FF00FF))
        ) {
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