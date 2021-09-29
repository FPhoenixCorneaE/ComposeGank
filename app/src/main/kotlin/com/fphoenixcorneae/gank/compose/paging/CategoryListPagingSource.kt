package com.fphoenixcorneae.gank.compose.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.ext.loge
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.network.service.GankService

/**
 * @desc：分类列表 PagingSource
 * @date：2021/09/28 15:08
 */
class CategoryListPagingSource(
    private val gankService: GankService,
    private val category: String,
    private val type: String,
    private val count: Int,
) : PagingSource<Int, CategoryListBean.Data>() {

    override fun getRefreshKey(state: PagingState<Int, CategoryListBean.Data>): Int {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryListBean.Data> {
        return try {
            // 未定义时加载第 1 页
            val page = params.key ?: 1
            val data = gankService.getCategoryList(
                category = category,
                type = type,
                page = page,
                count = count
            )
            LoadResult.Page(
                data = data.data,
                prevKey = null,
                nextKey = page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}