package com.fphoenixcorneae.gank.compose.mvvm.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：分类
 * @date：2021/09/24 17:27
 */
@Keep
class CategoryBean : ArrayList<CategoryBean.Data>() {
    @Keep
    data class Data(
        @SerializedName("coverImageUrl")
        val coverImageUrl: String? = null,
        @SerializedName("desc")
        val desc: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("type")
        val type: String? = null
    )
}