package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.fphoenixcorneae.ext.isNotNullOrEmpty
import com.fphoenixcorneae.ext.isNull
import com.fphoenixcorneae.ext.startKtxActivity
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Constant
import com.fphoenixcorneae.gank.compose.ext.*
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.mvvm.view.activity.PostDetailActivity
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.theme.typography
import com.fphoenixcorneae.util.ColorUtil
import kotlinx.coroutines.launch

/**
 * ????????????
 */
@Composable
fun CategoryListScreen(
    context: Context
) {
    CategoryList(context = context)
}

@Composable
private fun CategoryList(
    context: Context,
    gankViewModel: GankViewModel = viewModel(),
) {
    val categoryList = gankViewModel.categoryList?.collectAsLazyPagingItems()
    if (categoryList.isNull()) {
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 68.dp)
    ) {
        itemsIndexed(items = categoryList!!) { _, item ->
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

@Composable
fun CategoryListItem(context: Context, categoryListItemData: CategoryListBean.Data?) {
    // Scale Animation
    val animatedModifier = run {
        val animatedProgress = remember { Animatable(initialValue = 0.8f) }
        LaunchedEffect(Unit) {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, easing = LinearEasing)
            )
        }
        Modifier
            .padding(top = 16.dp)
            .graphicsLayer(scaleY = animatedProgress.value, scaleX = animatedProgress.value)
    }
    Row(modifier = animatedModifier) {
        AuthorImage()
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    coroutineScope.launch {
                        // ??????????????????
                        context.startKtxActivity<PostDetailActivity>(
                            extra = Bundle().apply {
                                putString(Constant.POST_ID, categoryListItemData?.id)
                                putString(Constant.TITLE, categoryListItemData?.title)
                            },
                            value = null
                        )
                    }
                }
        ) {
            AuthorNameAndPublishedTime(
                authorName = categoryListItemData?.author ?: "",
                publishedAt = categoryListItemData?.publishedAt ?: ""
            )
            Text(
                text = categoryListItemData?.desc ?: "",
                style = typography.body1,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .fillMaxWidth()
            )
            Images(images = categoryListItemData?.images)
            IconSection(
                watchCount = categoryListItemData?.views,
                starCount = categoryListItemData?.stars,
                likeCount = categoryListItemData?.likeCounts
            )
            Divider(thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AuthorImage() {
    val painter = rememberImagePainter(
        data = ColorDrawable(ColorUtil.randomColor),
        builder = { crossfade(true) }
    )
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(start = 16.dp)
            .size(50.dp)
            .clip(CircleShape)
            .border(
                shape = CircleShape,
                border = BorderStroke(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            blue0x5851db,
                            purple0x833ab4,
                            orange0xf56040,
                            yellow0xfcaf45
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(100f, 100f)
                    )
                )
            )
    )
}

@Composable
fun AuthorNameAndPublishedTime(
    authorName: String,
    publishedAt: String,
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
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
fun Images(
    images: List<String?>?
) {
    if (images.isNotNullOrEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in images!!.indices) {
                val painter = rememberImagePainter(
                    data = images[i],
                    builder = {
                        crossfade(true)
                        error(ColorDrawable(ColorUtil.randomColor))
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun IconSection(
    watchCount: Int?,
    starCount: Int?,
    likeCount: Int?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val coroutineScope = rememberCoroutineScope()
        Row(modifier = Modifier
            .clickable { coroutineScope.launch { } }
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
                text = watchCount?.friendlyViewsCount().toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable { coroutineScope.launch { } }
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
                text = starCount?.friendlyViewsCount().toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable { coroutineScope.launch { } }
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
                text = likeCount?.friendlyViewsCount().toString(),
                modifier = Modifier.padding(start = 8.dp),
                color = gray0x666666,
                style = typography.body2
            )
        }
        Row(modifier = Modifier
            .clickable { coroutineScope.launch { } }
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
        context = LocalContext.current,
        CategoryListBean.Data(
            author = "24k-?????????",
            publishedAt = "2021-04-14 01:00:26",
            desc = "??????????????????Android?????????,???activity???fragment???????????????,??????star",
            views = 4900,
            stars = 158,
            likeCounts = 98,
        )
    )
}