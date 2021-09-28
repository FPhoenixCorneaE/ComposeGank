package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.view.CategoryListScreen
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity
import com.fphoenixcorneae.jetpackmvvm.compose.uistate.UiState
import kotlinx.coroutines.flow.collect

/**
 * @desc：分类列表
 * @date：2021/09/27 10:46
 */
class CategoryListActivity : BaseActivity() {

    private val mGankViewModel by viewModels<GankViewModel>()
    private val mCategory: String by lazy {
        intent?.getStringExtra(CATEGORY) ?: Category.Article.name
    }
    private val mType: String by lazy {
        intent?.getStringExtra(TYPE) ?: "Android"
    }

    override fun initView() {
        setRealContent {
            CategoryListScreen(
                context = getLocalContext(),
                gankViewModel = mGankViewModel,
                title = "$mCategory-$mType",
                onToolbarClick = onToolbarClick
            )
        }
    }

    override fun initListener() {
        lifecycleScope.launchWhenResumed {
            mGankViewModel.uiState.collect {
                when (it) {
                    is UiState.ShowError -> uiStateViewModel.showEmpty(it.errorMsg)
                    is UiState.ShowLoading -> uiStateViewModel.showLoading(it.loadingMsg)
                    is UiState.ShowEmpty -> uiStateViewModel.showEmpty(it.emptyMsg)
                    is UiState.ShowNoNetwork -> uiStateViewModel.showNoNetwork(it.imageData, it.noNetworkMsg)
                    else -> uiStateViewModel.showContent()
                }
            }
        }
    }

    override fun initData() {
        mGankViewModel.getCategoryList(mCategory, mType)
    }

    override fun toolbarVisible() = false

    companion object {
        private const val CATEGORY = "category"
        private const val TYPE = "type"

        fun start(context: Context, category: String?, type: String?) {
            val intent = Intent(context, CategoryListActivity::class.java).apply {
                putExtra(CATEGORY, category)
                putExtra(TYPE, type)
            }
            context.startActivity(intent)
        }
    }
}