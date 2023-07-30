package com.google.android.apps.markers.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Text(text = "Colors", modifier = Modifier.width(144.dp))
            Row {
                SmallButton(
                    onClick = {
                        viewModel.penState = viewModel.penState.copy(color = Color.Black)
                    },
                ) {
                    Text(
                        text = "black",
                        modifier = Modifier
                            .width(48.dp)
                            .background(Color(0xFF000000))
                    )
                }
                SmallButton(
                    onClick = {
                        viewModel.penState = viewModel.penState.copy(color = Color.White)
                    },
                ) {
                    Text(
                        text = "white",
                        modifier = Modifier
                            .width(48.dp)
                            .background(Color(0xFFFFFFFF))
                    )
                }
                SmallButton(
                    onClick = {
                        viewModel.penState = viewModel.penState.copy(color = Color.Red)
                    },
                ) {
                    Text(
                        text = "red",
                        modifier = Modifier
                            .width(48.dp)
                            .background(Color(0xFFFF0000))
                    )
                }
            }
        }
    }
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