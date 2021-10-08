package com.fphoenixcorneae.gank.compose.network.service

import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.mvvm.model.HomepageBannersBean
import com.fphoenixcorneae.gank.compose.mvvm.model.PostDetailBean
import com.fphoenixcorneae.gank.compose.network.ApiResponse
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

        // 请求成功
        const val REQUEST_OK = 100
    }

    /**
     * 获取首页 banner 轮播
     */
    @GET("/api/v2/banners")
    suspend fun getHomepageBanners(): ApiResponse<HomepageBannersBean>

    /**
     * 获取分类
     */
    @GET("/api/v2/categories/{category}")
    suspend fun getCategories(
        @Path("category") category: String
    ): ApiResponse<CategoryBean>

    /**
     * 获取分类列表数据
     */
    @GET("/api/v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun getCategoryList(
        @Path("category") category: String,
        @Path("type") type: String,
        @Path("page") page: Int,
        @Path("count") count: Int,
    ): ApiResponse<CategoryListBean>

    /**
     * 获取文章详情
     */
    @GET("/api/v2/post/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: String
    ): ApiResponse<PostDetailBean>

    /**
     * 获取文章评论
     */
    @GET("/api/v2/comments/{postId}")
    suspend fun getPostComments(
        @Path("postId") postId: String
    ): ApiResponse<Any?>

    /**
     * 获取本周最热
     */
    @GET("/api/v2/hot/{hotType}/category/{category}/count/{count}")
    suspend fun getThisWeekHottest(
        @Path("hotType") hotType: String,
        @Path("category") category: String,
        @Path("count") count: Int,
    ): ApiResponse<CategoryListBean>
}