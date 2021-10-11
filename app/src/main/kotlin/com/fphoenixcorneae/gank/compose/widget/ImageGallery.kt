package com.fphoenixcorneae.gank.compose.widget

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

/**
 * 图像画廊
 */
@Composable
fun ImageGallery(
    modifier: Modifier = Modifier,
    images: List<Any?>,
    currentPage: Int = 0,
    onItemClick: (position: Int) -> Unit
) {
    val pagerState = run {
        remember { PagerState(currentPage = currentPage, minPage = 0, maxPage = images.size - 1) }
    }
    val selectedPage = remember { mutableStateOf(currentPage) }

    Pager(state = pagerState, modifier = modifier) {
        selectedPage.value = pagerState.currentPage
        ImageItem(
            data = images[page],
            size = images.size,
            currentPage = pagerState.currentPage,
            onItemClick = onItemClick
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageItem(data: Any?, size: Int, currentPage: Int, onItemClick: (position: Int) -> Unit) {
    Box {
        val painter = rememberImagePainter(
            data = data,
            builder = {
                crossfade(true)
                error(ColorDrawable(Color.LightGray.toArgb()))
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onItemClick(currentPage)
                },
            contentScale = ContentScale.Crop,
        )

        Text(
            text = "${currentPage + 1}/$size",
            style = typography.h6.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomStart),
        )
    }
}