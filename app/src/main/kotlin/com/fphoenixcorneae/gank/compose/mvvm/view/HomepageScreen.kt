package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.fphoenixcorneae.bannerlayout.annotation.TipsProgressesSiteMode
import com.fphoenixcorneae.bannerlayout.annotation.TipsTitleSiteMode
import com.fphoenixcorneae.bannerlayout.imagemanager.GlideImageManager
import com.fphoenixcorneae.bannerlayout.listener.BannerModelCallBack
import com.fphoenixcorneae.bannerlayout.widget.BannerLayout
import com.fphoenixcorneae.bannerlayout.widget.ProgressDrawable
import com.fphoenixcorneae.ext.dp2px
import com.fphoenixcorneae.ext.isNotNullOrEmpty
import com.fphoenixcorneae.ext.view.gone
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.widget.Toolbar

/**
 * 首页
 */
@Composable
fun HomepageScreen(
    localContext: Context,
    gankViewModel: GankViewModel,
    onToolbarClick: ((View, Int, CharSequence?) -> Unit)?
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 标题栏
        Toolbar(onToolbarClick = onToolbarClick) {
            // 设置标题栏属性
            leftImageButton.gone()
            centerText = localContext.getString(R.string.app_name)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(state = rememberScrollState())
        ) {
            val homepageBanners = gankViewModel.homepageBanners.collectAsState()
            if (homepageBanners.value.isNotEmpty()) {
                val imageList = mutableListOf<BannerModelCallBack<*>>()
                for (data in homepageBanners.value) {
                    if (data is BannerModelCallBack<*>) {
                        imageList.add(data)
                    }
                }
                // 首页 banner 轮播
                HomepageBanner(
                    context = localContext,
                    imageList = imageList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            // 分类
            Categories(gankViewModel = gankViewModel)
        }
    }
}

/**
 * 首页 banner 轮播
 */
@Composable
fun HomepageBanner(
    context: Context,
    imageList: List<BannerModelCallBack<*>>?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 12.dp, end = 16.dp)
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
                initListResources(imageList)
            }
        }
        AndroidView(
            { bannerLayout },
            modifier = modifier
        ) {
            it.startRotation(true)
        }
    }
}

/**
 * 分类
 */
@Composable
fun Categories(gankViewModel: GankViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // 文章
        CategoryTitle(Category.Article.type)
        val article = gankViewModel.articleCategories.collectAsState()
        CategoryItem(categoryDatas = article.value)
        // 干货
        CategoryTitle(Category.GanHuo.type)
        val ganHuo = gankViewModel.ganHuoCategories.collectAsState()
        CategoryItem(categoryDatas = ganHuo.value)
        // 妹纸
        CategoryTitle(Category.Girl.type)
        val girl = gankViewModel.girlCategories.collectAsState()
        CategoryItem(categoryDatas = girl.value)
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
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(color = Color(0x50888888), shape = MaterialTheme.shapes.medium)
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
    )
}

/**
 * 分类项
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CategoryItem(categoryDatas: List<CategoryBean.Data?>?) {
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
                    val painter = rememberImagePainter(
                        data = item?.coverImageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(GradientDrawable().apply {
                                setColor(Color(0x50888888).toArgb())
                                cornerRadius = LocalDensity.current.run { 8.dp.toPx() }
                            })
                            transformations(
                                RoundedCornersTransformation(
                                    topLeft = LocalDensity.current.run { 8.dp.toPx() },
                                    topRight = LocalDensity.current.run { 8.dp.toPx() },
                                    bottomLeft = LocalDensity.current.run { 8.dp.toPx() },
                                    bottomRight = LocalDensity.current.run { 8.dp.toPx() },
                                )
                            )
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = item?.desc,
                        modifier = Modifier
                            .width(200.dp)
                            .height(120.dp)
                    )

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