package com.google.android.apps.markers.compose

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.apps.markers.Slate
import com.google.android.apps.markers.compose.theme.MarkersTheme
import org.dsandler.apps.markers.R

class MarkersBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarkersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MarkersSlateView()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@Composable
fun MarkersSlateView() {
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            Slate(context)
        },
        update = { view ->
            view.setBackgroundColor(android.graphics.Color.RED)
            view.setPenColor(android.graphics.Color.BLACK)
            view.setDrawingBackground(android.graphics.Color.YELLOW)
            view.setPenSize(1f, 50f)
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary
            view.addOnAttachStateChangeListener(
            object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {
//                    view.debugFlags = Slate.FLAG_DEBUG_EVERYTHING
                }

                override fun onViewDetachedFromWindow(v: View?) {
                }
            })
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MarkersBoardPreview() {
    MarkersTheme {
        TopAppBar { Text("Markers-Compose") }
        Image(
            painter = BitmapPainter(
                ImageBitmap.imageResource(id = R.drawable.icon)
            ),
            contentDescription = "Markers logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
        Palette()
    }
}

@Composable
fun Palette(modifier: Modifier = Modifier) {
//    BorderStroke(width = 2.dp, brush = SolidColor(value = Color(0xFF0000.toInt()))) {
        Box(
            modifier = modifier
                .wrapContentSize(align = Alignment.TopCenter)
                .background(color = Color(0xFF00FFFF))
                .border(width = 2.dp, color = Color(0xFF000000))
                .padding(4.dp)
        ) {
             Column {
                 Text(text = "Colors", modifier = Modifier.width(144.dp))
                 Row {
                     Text(
                         text = "black",
                         modifier = Modifier
                             .width(48.dp)
                             .background(Color(0xFF000000))
                     )
                     Text(
                         text = "white",
                         modifier = Modifier
                             .width(48.dp)
                             .background(Color(0xFFFFFFFF))
                     )
                     Text(
                         text = "red",
                         modifier = Modifier
                             .width(48.dp)
                             .background(Color(0xFFFF0000))
                     )
                 }
            }
        }
//    }
}

