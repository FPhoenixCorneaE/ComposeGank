package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.fphoenixcorneae.ext.isNotNull
import com.fphoenixcorneae.ext.isNotNullOrEmpty
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.ext.view.gone
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.ext.gray0x666666
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.theme.typography
import com.fphoenixcorneae.jetpackmvvm.compose.widget.Toolbar
import com.fphoenixcorneae.util.ColorUtil

/**
 * 分类列表
 */
@Composable
fun CategoryListScreen(
    context: Context,
    gankViewModel: GankViewModel,
    title: String,
    onToolbarClick: ((View, Int, CharSequence?) -> Unit)?
) {
    val titleText = remember { mutableStateOf(title) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 标题栏
        Toolbar(onToolbarClick = onToolbarClick) {
            // 设置标题栏属性
            centerText = titleText.value
            centerTextSize = 20f.spToPx()
        }

        CategoryList(
            context = context,
            gankViewModel = gankViewModel
        )
    }
}

@Composable
private fun CategoryList(
    context: Context,
    gankViewModel: GankViewModel,
) {
    val categoryList = gankViewModel.categoryList.collectAsState()
    if (categoryList.value.isNullOrEmpty()) {
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items = categoryList.value) { _: Int, item ->
            CategoryListItem(categoryListItemData = item)
        }
    }
}

@Composable
private fun CategoryListItem(categoryListItemData: CategoryListBean.Data?) {
    // Scale Animation
    val animatedModifier = run {
        val animatedProgress = remember { Animatable(initialValue = 0.8f) }
        LaunchedEffect(Unit) {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, easing = LinearEasing)
            )
        }
        Modifier.graphicsLayer(scaleY = animatedProgress.value, scaleX = animatedProgress.value)
    }
    Row(modifier = animatedModifier) {
        AuthorImage()
        Column(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            AuthorNameAndPublishedTime(
                authorName = categoryListItemData?.author ?: "",
                publishedAt = categoryListItemData?.publishedAt ?: ""
            )
            Text(
                text = categoryListItemData?.desc ?: "",
                style = typography.body1,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            Images(images = categoryListItemData?.images)
            IconSection(
                watchCount = categoryListItemData?.views,
                starCount = categoryListItemData?.stars,
                likeCount = categoryListItemData?.likeCounts
            )
            Divider(thickness = 0.5.dp)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun AuthorImage() {
    val painter = rememberImagePainter(
        data = ColorDrawable(ColorUtil.randomColor),
        builder = { crossfade(true) }
    )
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(16.dp)
            .size(50.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun AuthorNameAndPublishedTime(
    authorName: String,
    publishedAt: String,
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (author, publishedTime) = createRefs()
        createHorizontalChain(author, publishedTime, chainStyle = ChainStyle.SpreadInside)
        // authorName
        Text(
            text = authorName,
            style = typography.h6,
            modifier = Modifier.constrainAs(author) {
                linkTo(top = parent.top, bottom = parent.bottom)
                linkTo(start = parent.start, end = publishedTime.start)
            }
        )
        // publishedAt
        Text(
            text = publishedAt,
            style = typography.body1.copy(color = gray0x666666, fontSize = 12.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(publishedTime) {
                linkTo(top = parent.top, bottom = parent.bottom)
                linkTo(start = author.end, end = parent.end)
            }
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun Images(
    images: List<String?>?
) {
    if (images.isNotNullOrEmpty() && images?.getOrNull(0).isNotNull()) {
        val painter = rememberImagePainter(
            data = images?.getOrNull(0),
            builder = {
                crossfade(true)
                error(ColorDrawable(ColorUtil.randomColor))
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun IconSection(
    watchCount: Int?,
    starCount: Int?,
    likeCount: Int?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier
            .clickable {}
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_watch),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = gray0x666666
            )
            Text(
                text = watchCount.toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable {}
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = gray0x666666
            )
            Text(
                text = starCount.toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable {}
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = gray0x666666
            )
            Text(
                text = likeCount.toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable {}
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = gray0x666666
            )
        }
    }

}

@Preview
@Composable
fun PreviewCategoryListItem() {
    CategoryListItem(
        CategoryListBean.Data(
            author = "24k-小清新",
            publishedAt = "2021-04-14 01:00:26",
            desc = "高仿喜马拉雅Android客户端,单activity多fragment组件化架构,欢迎star",
            views = 4900,
            stars = 158,
            likeCounts = 98,
        )
    )
}