package com.fphoenixcorneae.gank.compose.network.service

import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.model.HomepageBannersBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @desc：GankService
 * @date：2021/09/23 15:18
 */
interface GankService {

    companion object {
        // 干货集中营地址
        const val BASE_URL = "https://gank.io"
    }

    /**
     * 获取首页 banner 轮播
     */
    @GET("/api/v2/banners")
    suspend fun getHomepageBanners(): HomepageBannersBean

    /**
     * 获取分类
     */
    @GET("/api/v2/categories/{categoryType}")
    suspend fun getCategories(@Path("categoryType") categoryType: String): CategoryBean
}