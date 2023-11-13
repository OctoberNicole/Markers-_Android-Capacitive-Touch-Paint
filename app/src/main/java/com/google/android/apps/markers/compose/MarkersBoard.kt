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

import android.os.Bundle
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.google.android.apps.markers.Slate
import com.google.android.apps.markers.compose.theme.MarkersTheme
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

class MarkersBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = MarkersBoardViewModel()

        setContent {
            //MarkersTheme
            Box {
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
        MarkersSlateView(vm)
//        Image(
//            painter = BitmapPainter(
//                ImageBitmap.imageResource(id = R.drawable.icon)
//            ),
//            contentDescription = "Markers logo",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Fit,
//        )
        Palette(viewModel = vm,
            modifier = Modifier.wrapContentSize(align = Alignment.TopCenter))
    }
}

@Composable
fun MarkersSlateView(viewModel: MarkersBoardViewModel) {
    AndroidView(
        modifier = Modifier
//            .graphicsLayer()
            .fillMaxSize()
            .background(Color.Red)
            ,
        factory = { context ->
            Slate(context).also { view ->
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

