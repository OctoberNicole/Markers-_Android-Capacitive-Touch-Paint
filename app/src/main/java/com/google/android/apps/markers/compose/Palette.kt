package com.google.android.apps.markers.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun Palette(modifier: Modifier = Modifier.wrapContentSize(), viewModel: MarkersBoardViewModel) {
    Box(modifier = modifier) {
//        HyperBorder(
//            modifier = Modifier.matchParentSize()
//                .background(color = Color.Cyan)
//        )
        Column(modifier = Modifier.absolutePadding(2.dp, 2.dp, 4.dp, 4.dp)) {
            Text(text = "Colors", modifier = Modifier.width(144.dp), color = Color.Magenta)
            Row(
                modifier = Modifier
                    .border(3.dp, Color.Black, RoundedCornerShape(12.dp))
                    .padding(3.dp)
            ) {
                ColorSwatch(color = Color.Black, viewModel = viewModel)
                ColorSwatch(color = Color.White, viewModel = viewModel)
                ColorSwatch(color = Color.Red, viewModel = viewModel)
                ColorSwatch(color = Color.Blue, viewModel = viewModel)
            }
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
        .width(48.dp)
        .height(48.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(color)
        .clickable(onClick = {
            viewModel.penState = viewModel.penState.copy(color = color)
        })
    if (viewModel.penState.color == color) {
        m = m.border(
            width = if (viewModel.penState.color == color) 3.dp else 0.dp,
            color = if (color == Color.Black) Color.White else Color.Black,
            shape = RoundedCornerShape(8.dp)
        )
    }
    Box(modifier = m)
}

@Composable
fun SmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PalettePreview() {
    Palette(viewModel = MarkersBoardViewModel())
}