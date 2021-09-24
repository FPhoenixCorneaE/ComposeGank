package com.fphoenixcorneae.gank.compose.mvvm.model


import androidx.annotation.Keep
import com.fphoenixcorneae.bannerlayout.listener.BannerModelCallBack
import com.google.gson.annotations.SerializedName

/**
 * @desc：首页 banner 轮播
 * @date：2021/09/23 15:33
 */
@Keep
data class HomepageBannersBean(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("status")
    val status: Int? = null
) {
    @Keep
    data class Data(
        @SerializedName("image")
        val image: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("url")
        val url: String? = null
    ) : BannerModelCallBack<String?> {
        override val bannerTitle: String?
            get() = title
        override val bannerUrl: String?
            get() = image
    }
}