package com.fphoenixcorneae.gank.compose.mvvm.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Category

/**
 * 本周最热
 */
@Composable
fun ThisWeekHottestScreen() {
    Column(modifier = Modifier.padding(top = 68.dp)) {
        AvailableParams()
    }
}

/**
 * 可用参数
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AvailableParams() {
    val availableCategory = listOf(Category.Article.name, Category.GanHuo.name, Category.Girl.name)
    val availableType = listOf(stringResource(R.string.views_count), stringResource(R.string.likes_count))
    Column {
        // 分类
        val selectedCategoryIndex = remember { mutableStateOf(0) }
        LazyVerticalGrid(
            cells = GridCells.Fixed(availableCategory.size),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            itemsIndexed(items = availableCategory) { index, title ->
                ChipCells(
                    selected = index == selectedCategoryIndex.value,
                    text = title,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedCategoryIndex.value = index
                        })
            }
        }
        // 类型
        val selectedTypeIndex = remember { mutableStateOf(0) }
        LazyVerticalGrid(
            cells = GridCells.Fixed(availableType.size),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            itemsIndexed(items = availableType) { index, title ->
                ChipCells(
                    selected = index == selectedTypeIndex.value,
                    text = title,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedTypeIndex.value = index
                        })
            }
        }
    }
}

@Composable
private fun ChipCells(
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface.copy(
            alpha = if (selected) {
                if (MaterialTheme.colors.isLight) 0.7f else 1f
            } else {
                if (MaterialTheme.colors.isLight) 0.04f else 0.07f
            }
        ),
        contentColor = if (selected) {
            MaterialTheme.colors.surface
        } else {
            MaterialTheme.colors.onSurface
        },
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> {
                    MaterialTheme.colors.surface
                }
                MaterialTheme.colors.isLight -> {
                    Color.LightGray
                }
                else -> {
                    Color.DarkGray
                }
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            )
        )
    }
}