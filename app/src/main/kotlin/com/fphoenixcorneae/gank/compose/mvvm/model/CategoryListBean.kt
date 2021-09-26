package com.fphoenixcorneae.gank.compose.mvvm.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：分类列表数据
 * @date：2021/09/26 16:19
 */
@Keep
data class CategoryListBean(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("page_count")
    val pageCount: Int? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("total_counts")
    val totalCounts: Int? = null
) {
    @Keep
    data class Data(
        @SerializedName("author")
        val author: String? = null,
        @SerializedName("category")
        val category: String? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("desc")
        val desc: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("images")
        val images: List<String?>? = null,
        @SerializedName("likeCounts")
        val likeCounts: Int? = null,
        @SerializedName("publishedAt")
        val publishedAt: String? = null,
        @SerializedName("stars")
        val stars: Int? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("views")
        val views: Int? = null
    )
}