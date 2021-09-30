package com.fphoenixcorneae.gank.compose.mvvm.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：文章详情
 * @date：2021/09/29 17:57
 */
@Keep
data class PostDetailBean(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("images")
    val images: List<String?>? = null,
    @SerializedName("index")
    val index: Int? = null,
    @SerializedName("isOriginal")
    val isOriginal: Boolean? = null,
    @SerializedName("license")
    val license: String? = null,
    @SerializedName("likeCount")
    val likeCount: Int? = null,
    @SerializedName("likeCounts")
    val likeCounts: Int? = null,
    @SerializedName("likes")
    val likes: List<String?>? = null,
    @SerializedName("markdown")
    val markdown: String? = null,
    @SerializedName("originalAuthor")
    val originalAuthor: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
    @SerializedName("stars")
    val stars: Int? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("tags")
    val tags: List<Any?>? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("views")
    val views: Int? = null
)