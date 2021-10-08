package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.util.ScreenUtil
import kotlinx.coroutines.launch

/**
 * 本周最热
 */
@Composable
fun ThisWeekHottestScreen(
    context: Context,
    category: String,
    type: String,
) {
    ThisWeekHottest(context = context, category = category, type = type)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ThisWeekHottest(
    context: Context,
    category: String,
    type: String,
    gankViewModel: GankViewModel = viewModel(),
) {
    val thisWeekHottest = gankViewModel.thisWeekHottest.collectAsState()
    if (thisWeekHottest.value.isNullOrEmpty()) {
        return
    }
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 68.dp),
        state = state,
    ) {
        stickyHeader {
            AvailableParams(category = category, type = type, state)
        }
        itemsIndexed(items = thisWeekHottest.value) { _, item ->
            CategoryListItem(context = context, categoryListItemData = item)
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }
    }
}

/**
 * 可用参数
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AvailableParams(
    category: String,
    type: String,
    state: LazyListState,
    gankViewModel: GankViewModel = viewModel()
) {
    val availableCategory = mutableListOf(Category.Article.name, Category.GanHuo.name, Category.Girl.name)
    val availableType = mutableListOf(stringResource(R.string.views_count), stringResource(R.string.likes_count))
    val hotType = mutableListOf("views", "likes")
    val selectedCategory = remember { mutableStateOf(category) }
    val selectedType = remember { mutableStateOf(type) }
    val selectedCategoryIndex = remember { mutableStateOf(availableCategory.indexOf(category)) }
    val selectedTypeIndex = remember { mutableStateOf(hotType.indexOf(type)) }

    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.background(color = Color.White)) {
        // 分类
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp)
                .horizontalScroll(state = rememberScrollState())
        ) {
            repeat(times = availableCategory.size) { index ->
                ChipCells(
                    selected = index == selectedCategoryIndex.value,
                    text = availableCategory[index],
                    modifier = Modifier
                        .width(LocalDensity.current.run {
                            ((ScreenUtil.screenWidth - 16 * availableCategory.size) / availableCategory.size).toDp()
                        })
                        .padding(start = 8.dp, top = 16.dp, end = 8.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            selectedCategoryIndex.value = index
                            selectedCategory.value = availableCategory[index]
                            gankViewModel.getThisWeekHottest(
                                hotType = selectedType.value,
                                category = selectedCategory.value,
                                showLoading = false
                            )
                            // scroll to top
                            coroutineScope.launch {
                                state.scrollToItem(index = 0)
                            }
                        }
                )
            }
        }
        // 类型
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp)
                .horizontalScroll(state = rememberScrollState())
        ) {
            repeat(times = availableType.size) { index ->
                ChipCells(
                    selected = index == selectedTypeIndex.value,
                    text = availableType[index],
                    modifier = Modifier
                        .width(LocalDensity.current.run {
                            ((ScreenUtil.screenWidth - 16 * availableCategory.size) / availableCategory.size).toDp()
                        })
                        .padding(8.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            selectedTypeIndex.value = index
                            selectedType.value = hotType[index]
                            gankViewModel.getThisWeekHottest(
                                hotType = selectedType.value,
                                category = selectedCategory.value,
                                showLoading = false
                            )
                            // scroll to top
                            coroutineScope.launch {
                                state.scrollToItem(index = 0)
                            }
                        }
                )
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
