package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.fphoenixcorneae.bannerlayout.annotation.TipsProgressesSiteMode
import com.fphoenixcorneae.bannerlayout.annotation.TipsTitleSiteMode
import com.fphoenixcorneae.bannerlayout.imagemanager.GlideImageManager
import com.fphoenixcorneae.bannerlayout.listener.BannerModelCallBack
import com.fphoenixcorneae.bannerlayout.widget.BannerLayout
import com.fphoenixcorneae.bannerlayout.widget.ProgressDrawable
import com.fphoenixcorneae.ext.dp2px
import com.fphoenixcorneae.ext.isNotNullOrEmpty
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.ext.graySurface
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.view.activity.CategoryListActivity
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.theme.typography
import kotlinx.coroutines.launch

/**
 * 首页
 */
@Composable
fun HomepageScreen(
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        // 首页 banner 轮播
        HomepageBanner(context = context)
        // 分类
        Categories(context = context)
    }
}

/**
 * 首页 banner 轮播
 */
@Composable
private fun HomepageBanner(
    context: Context,
    gankViewModel: GankViewModel = viewModel()
) {
    val homepageBanners = gankViewModel.homepageBanners.collectAsState()
    if (homepageBanners.value.isEmpty()) {
        return
    }
    val bannerImages = mutableListOf<BannerModelCallBack<*>>()
    for (data in homepageBanners.value) {
        if (data is BannerModelCallBack<*>) {
            bannerImages.add(data)
        }
    }
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 84.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.Transparent,
        elevation = 8.dp,
    ) {
        val bannerLayout = remember {
            BannerLayout(context).apply {
                // 初始化指示器显示与否
                initTips(
                    isBackgroundColor = false,
                    isVisibleDots = false,
                    isVisibleProgresses = true,
                    isVisibleTitle = true
                )
                // 动画时间
                setDuration(2_000)
                // 翻页间隔
                setDelayTime(800)
                // viewpager 是否能翻页, true 为不能, false 为可以
                setViewPagerTouchMode(true)
                // 图片加载管理器
                setImageLoaderManager(GlideImageManager(dp2px(8f)))
                // 初始化进度指示器
                setProgressesMargin(2.5f, 0f, 2.5f, 40f)
                setProgressesBuilder(
                    ProgressDrawable.Builder(context)
                        .setWidth(40f)
                        .setHeight(2.5f)
                        .setDuration(2_000)
                        .setBackgroundColor(Color.White.toArgb())
                        .setProgressColor(Color.Red.toArgb())
                        .setRadius(8f)
                        .setAnimated(true)
                )
                setProgressesSite(TipsProgressesSiteMode.BOTTOM, TipsProgressesSiteMode.CENTER_HORIZONTAL)
                // 初始化标题
                setTitleTextColor(Color.White.toArgb())
                setTitleTextSize(13f)
                setTitleMargin(8f)
                setTitleBackgroundColor(0x50000000)
                setTitleSite(TipsTitleSiteMode.BOTTOM)
                // 初始化数据,须在最后
                initListResources(bannerImages)
            }
        }
        AndroidView(
            { bannerLayout },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            it.startRotation(true)
        }
    }
}

/**
 * 分类
 */
@Composable
private fun Categories(
    context: Context,
    gankViewModel: GankViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // 文章
        CategoryTitle(Category.Article.name)
        val article = gankViewModel.articleCategories.collectAsState()
        CategoryItem(context = context, categoryDatas = article.value, category = Category.Article)
        // 干货
        CategoryTitle(Category.GanHuo.name)
        val ganHuo = gankViewModel.ganHuoCategories.collectAsState()
        CategoryItem(context = context, categoryDatas = ganHuo.value, category = Category.GanHuo)
        // 妹纸
        CategoryTitle(Category.Girl.name)
        val girl = gankViewModel.girlCategories.collectAsState()
        CategoryItem(context = context, categoryDatas = girl.value, category = Category.Girl)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * 分类标题
 */
@Composable
private fun CategoryTitle(title: String) {
    Text(
        text = title,
        style = typography.h5.copy(fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Cursive),
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp)
    )
}

/**
 * 分类项
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
private fun CategoryItem(
    context: Context,
    categoryDatas: List<CategoryBean.Data?>?,
    category: Category? = null
) {
    if (categoryDatas.isNotNullOrEmpty()) {
        LazyRow(
            modifier = Modifier.wrapContentWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(items = categoryDatas!!) { _, item ->
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val cardColor = if (isSystemInDarkTheme()) graySurface else MaterialTheme.colors.background
                    val coroutineScope = rememberCoroutineScope()
                    Card(
                        elevation = 8.dp,
                        backgroundColor = cardColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                coroutineScope.launch {
                                    // 跳转分类列表
                                    CategoryListActivity.start(
                                        context = context,
                                        category = category?.name,
                                        type = item?.type
                                    )
                                }
                            }
                    ) {
                        val painter = rememberImagePainter(
                            data = item?.coverImageUrl,
                            builder = { crossfade(true) }
                        )
                        Image(
                            painter = painter,
                            contentDescription = item?.desc,
                            modifier = Modifier
                                .width(200.dp)
                                .height(120.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    item?.desc?.let {
                        Text(
                            text = it,
                            color = Color.Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(200.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCategories() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // 文章
        CategoryTitle(title = "Article")
        CategoryItem(
            context = LocalContext.current,
            categoryDatas = mutableListOf(CategoryBean.Data())
        )
        // 干货
        CategoryTitle(title = "GanHuo")
        CategoryItem(
            context = LocalContext.current,
            categoryDatas = mutableListOf(CategoryBean.Data())
        )
        // 妹纸
        CategoryTitle(title = "Girl")
        CategoryItem(
            context = LocalContext.current,
            categoryDatas = mutableListOf(CategoryBean.Data())
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}