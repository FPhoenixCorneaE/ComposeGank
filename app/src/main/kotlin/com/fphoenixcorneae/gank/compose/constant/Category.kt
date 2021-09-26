package com.fphoenixcorneae.gank.compose.constant

import com.fphoenixcorneae.ext.appContext
import com.fphoenixcorneae.gank.compose.R

/**
 * @desc：Category
 * @date：2021/09/24 17:41
 */
enum class Category(val type: String) {
    Article(appContext.getString(R.string.category_article)),
    GanHuo(appContext.getString(R.string.category_ganhuo)),
    Girl(appContext.getString(R.string.category_girl)),
}