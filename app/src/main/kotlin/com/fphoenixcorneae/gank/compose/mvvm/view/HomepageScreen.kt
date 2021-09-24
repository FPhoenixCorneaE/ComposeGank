package com.fphoenixcorneae.gank.compose.mvvm.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fphoenixcorneae.bannerlayout.annotation.TipsProgressesSiteMode
import com.fphoenixcorneae.bannerlayout.annotation.TipsTitleSiteMode
import com.fphoenixcorneae.bannerlayout.imagemanager.GlideImageManager
import com.fphoenixcorneae.bannerlayout.listener.BannerModelCallBack
import com.fphoenixcorneae.bannerlayout.widget.BannerLayout
import com.fphoenixcorneae.bannerlayout.widget.ProgressDrawable
import com.fphoenixcorneae.ext.dp2px
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel

/**
 * 首页
 */
@Composable
fun HomepageScreen(localContext: Context, gankViewModel: GankViewModel) {
    val homepageBanners = gankViewModel.homepageBanners.collectAsState()
    if (homepageBanners.value.isNotEmpty()) {
        val imageList = mutableListOf<BannerModelCallBack<*>>()
        for (data in homepageBanners.value) {
            if (data is BannerModelCallBack<*>) {
                imageList.add(data)
            }
        }
        // 首页 banner 轮播
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.Transparent,
            elevation = 8.dp,
        ) {
            HomepageBanner(
                context = localContext,
                imageList = imageList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
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