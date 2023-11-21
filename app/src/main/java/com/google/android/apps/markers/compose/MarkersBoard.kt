/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.apps.markers.compose

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import com.google.android.apps.markers.MarkersActivity
import com.google.android.apps.markers.MarkersUtils
import com.google.android.apps.markers.MediaClient
import com.google.android.apps.markers.Slate
import com.google.android.apps.markers.compose.theme.MarkersTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.dsandler.apps.markers.R

const val DEBUG = false

data class PenState(
    val color: Color = Color.Black,
    val widthMin: Float = 1f,
    val widthMax: Float = 25f,
)

class MarkersBoardViewModel() : ViewModel() {
    var penState by mutableStateOf(PenState())
}

enum class ClickEventType {
    CLEAR,
    SAVE
}

@Preview(showBackground = true)
@Composable
fun MarkersScene() {
    val vm = MarkersBoardViewModel()

    val scope = rememberCoroutineScope()
    val clickEvent = Channel<ClickEventType>(capacity = 1)

    MarkersSlateView(viewModel = vm, onClick = clickEvent)
    Box(modifier = Modifier.fillMaxSize()) {
        Palette(modifier = Modifier
            .safeContentPadding()
            .wrapContentSize()
            .align(Alignment.TopEnd)
        ) {
            Row {
                SmallButton(onClick = {
                    scope.launch { clickEvent.send(ClickEventType.CLEAR) }
                }) {
                    Image(
                        painter = painterResource(R.drawable.scribble),
                        contentDescription = "Clear",
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                SmallButton(onClick = {
                    scope.launch { clickEvent.send(ClickEventType.SAVE) }
                }) {
                    Image(
                        painter = painterResource(R.drawable.check),
                        contentDescription = "Save",
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
        PenPalette(
            viewModel = vm,
            modifier = Modifier
                .safeContentPadding() // will always avoid transient system bars
                .wrapContentSize()
                .align(Alignment.TopStart)
        )
        ColorPalette(
            viewModel = vm,
            modifier = Modifier
                .safeContentPadding() // will always avoid transient system bars
                .wrapContentSize(align = Alignment.TopCenter)
                .align(Alignment.BottomStart)
        )
    }
}

class MarkersBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val wiController = WindowInsetsControllerCompat(window, window.decorView)

        // Transient (translucent) status bars will appear on the first swipe, but will not
        // be interactive until the second touch
        wiController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        wiController.isAppearanceLightStatusBars = true
        /*
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
//                binding.toggleFullscreenButton.setOnClickListener {
//                    // Hide both the status bar and the navigation bar.
//                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
//                }

// if using the default systemBarsBehavior, you'll want this:
//                view.postDelayed({
//                     wiController.hide(WindowInsetsCompat.Type.systemBars())
//                }, 1000L)
            } else {
//                binding.toggleFullscreenButton.setOnClickListener {
//                    // Show both the status bar and the navigation bar.
//                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
//                }
            }
            view.onApplyWindowInsets(windowInsets)
        }
         */

        wiController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            MarkersTheme {
                MarkersScene()
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MarkersBoardPreview() {
    MarkersTheme {
        MarkersScene()
    }
}

fun saveBitmap(
    slate: Slate,
    bitmap: Bitmap,
    filename: String?,
    temporary: Boolean,
    animate: Boolean,
    share: Boolean,
    clear: Boolean
) {
    MediaClient.saveBitmap(
        slate.context,
        bitmap,
        true,
        null,
        -1,
        -1,
        filename,
        temporary,
        animate,
        share,
        clear,
        slate
    )
}

fun saveDrawing(slate: Slate) {
    if (slate.isEmpty) return
    //v.isEnabled = false
    val filename = MarkersUtils.createDrawingFilename()
    val localBits: Bitmap = slate.copyBitmap(true)
    saveBitmap(slate, localBits, filename, false, false, false, false)
    Toast.makeText(slate.context, "Drawing saved: $filename", Toast.LENGTH_SHORT).show()
    //v.isEnabled = true
}

@Composable
fun MarkersSlateView(viewModel: MarkersBoardViewModel, onClick: Channel<ClickEventType>) {
    val scope = rememberCoroutineScope()
    AndroidView(
        modifier = Modifier
//            .graphicsLayer()
            .fillMaxSize()
            .background(Color.Red),
        factory = { context ->
            Slate(context).also { view ->
                scope.launch {
                    //onClick.consume {
                    for (event in onClick) {
                        when (event) {
                            ClickEventType.CLEAR -> view.clear()
                            ClickEventType.SAVE -> saveDrawing(view)
                        }
                    }
                }
                view.background = context.getDrawable(R.drawable.transparent)
                if (DEBUG) {
                    view.setBackgroundColor(android.graphics.Color.RED)
                    view.setDrawingBackground(android.graphics.Color.YELLOW)
                    view.addOnAttachStateChangeListener(
                        object : OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: View) {
                                view.debugFlags = Slate.FLAG_DEBUG_EVERYTHING
                            }

                            override fun onViewDetachedFromWindow(v: View) {
                            }
                        })
                }
            }
        },
        update = { view ->
            view.setPenColor(viewModel.penState.color.toArgb())
            view.setPenSize(viewModel.penState.widthMin, viewModel.penState.widthMax)
        },
    )
}

