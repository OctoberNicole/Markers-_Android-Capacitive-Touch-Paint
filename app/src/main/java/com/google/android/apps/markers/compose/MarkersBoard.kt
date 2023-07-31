package com.google.android.apps.markers.compose

import android.os.Bundle
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.google.android.apps.markers.Slate
import com.google.android.apps.markers.compose.theme.MarkersTheme
import org.dsandler.apps.markers.R

data class PenState(
    val color: Color = Color.Black,
    val widthMin: Float = 1f,
    val widthMax: Float = 25f,
)

class MarkersBoardViewModel() : ViewModel() {
    var penState by mutableStateOf(PenState())
}

class MarkersBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = MarkersBoardViewModel()

        setContent {
            MarkersTheme {
                MarkersSlateView(vm)
                Palette(
                    viewModel = vm,
                    modifier = Modifier.wrapContentSize(align = Alignment.TopCenter)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MarkersBoardPreview() {
    val vm = MarkersBoardViewModel()

    MarkersTheme {
        Image(
            painter = BitmapPainter(
                ImageBitmap.imageResource(id = R.drawable.icon)
            ),
            contentDescription = "Markers logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
        Palette(viewModel = vm,
            modifier = Modifier.wrapContentSize(align = Alignment.TopCenter))
    }
}

@Composable
fun MarkersSlateView(viewModel: MarkersBoardViewModel) {
    AndroidView(
        modifier = Modifier
//            .graphicsLayer()
            .fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            Slate(context).also { view ->
                view.setBackgroundColor(android.graphics.Color.RED)
                view.setDrawingBackground(android.graphics.Color.YELLOW)
                view.addOnAttachStateChangeListener(
                    object : OnAttachStateChangeListener {
                        override fun onViewAttachedToWindow(v: View?) {
                            view.debugFlags = Slate.FLAG_DEBUG_EVERYTHING
                        }

                        override fun onViewDetachedFromWindow(v: View?) {
                        }
                    })
            }
        },
        update = { view ->
            view.setPenColor(viewModel.penState.color.toArgb())
            view.setPenSize(viewModel.penState.widthMin, viewModel.penState.widthMax)
        },
    )
}

