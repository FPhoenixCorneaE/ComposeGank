package com.fphoenixcorneae.gank.compose.ext

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 友好显示浏览数
 */
fun Int.friendlyViewsCount(): String {
    val views = BigDecimal(this)
    val moreThanTenThousands = this >= 10_000
    val friendlyViews = if (moreThanTenThousands) {
        views.divide(BigDecimal(1_000)).setScale(1, RoundingMode.FLOOR)
    } else {
        views
    }
    return if (moreThanTenThousands) {
        "${friendlyViews.toFloat()}k"
    } else {
        friendlyViews.toInt().toString()
    }
}