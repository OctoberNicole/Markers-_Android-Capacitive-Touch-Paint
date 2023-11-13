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

import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

val HYPER_LINE_THICKNESS = 4.dp

@Composable
fun Palette(modifier: Modifier = Modifier.wrapContentSize(), viewModel: MarkersBoardViewModel) {
    Box(
        modifier = modifier
            .hyperBorder(
                width = HYPER_LINE_THICKNESS,
                depth = HYPER_LINE_THICKNESS,
                cornerRadius = 2 * HYPER_LINE_THICKNESS,
                style = HyperStyle.Rounded)
            .background(color = Color(0xFFCCCCCC))
            .padding(12.dp)

    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorSwatch(color = Color.Black, viewModel = viewModel)
            ColorSwatch(color = Color.White, viewModel = viewModel)
            ColorSwatch(color = Color.Red, viewModel = viewModel)
            ColorSwatch(color = Color.Blue, viewModel = viewModel)
        }
    }
}

@Composable
fun ColorSwatch(
    color: Color,
    viewModel: MarkersBoardViewModel,
    modifier: Modifier = Modifier,
) {
    var m = modifier
        .background(color)
    if (viewModel.penState.color == color) {
        val borderWidth = 6.dp
        m = m.border(
            width = borderWidth,
            color = if (color == Color.Black) Color.Red else Color.Black,
            shape = RoundedCornerShape(3.dp)
        )
    }
    SmallButton(
        modifier = m,
        onClick = {
            viewModel.penState = viewModel.penState.copy(color = color)
        }
    )
}

@Composable
fun SmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .then(modifier)
    ) {
        content()
    }
}

@Preview(
    showBackground = false,
    widthDp = 500,
    heightDp = 100,
)
@Composable
fun PalettePreview() {
    Palette(viewModel = MarkersBoardViewModel())
}