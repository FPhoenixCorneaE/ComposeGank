package com.fphoenixcorneae.gank.compose.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.ext.isNotNullOrEmpty
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.network.service.GankService

/**
 * @desc：分类列表 PagingSource
 * @date：2021/09/28 15:08
 */
class CategoryListPagingSource(
    val gankService: GankService,
    val category: String,
    val type: String,
    val count: Int,
    val onSuccess: () -> Unit = {},
    val onEmpty: () -> Unit = {},
    val onError: (Exception) -> Unit = {},
) : PagingSource<Int, CategoryListBean.Data>() {

    override fun getRefreshKey(state: PagingState<Int, CategoryListBean.Data>): Int {
        return RefreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryListBean.Data> {
        return try {
            // 未定义时加载第 1 页
            val page = params.key ?: RefreshKey
            val data = gankService.getCategoryList(
                category = category,
                type = type,
                page = page,
                count = count
            )
            // 传 null 表示没有下一页
            val nextPage = when {
                data.data.isNotNullOrEmpty() -> {
                    onSuccess()
                    page + 1
                }
                page == RefreshKey -> {
                    onEmpty()
                    null
                }
                else -> {
                    null
                }
            }
            LoadResult.Page(
                data = data.data,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            onError(e)
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val RefreshKey = 1
    }
}