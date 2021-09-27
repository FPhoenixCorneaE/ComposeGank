package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.view.HomepageScreen
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity
import com.fphoenixcorneae.jetpackmvvm.compose.uistate.UiState
import kotlinx.coroutines.flow.collect

/**
 * @desc：MainActivity
 * @date：2021/09/22 17:15
 */
class MainActivity : BaseActivity() {

    private val mGankViewModel by viewModels<GankViewModel>()

    override fun initView() {
        setRealContent {
            HomepageScreen(
                localContext = getLocalContext(),
                gankViewModel = mGankViewModel,
                onToolbarClick
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
        mGankViewModel.getHomepageBanners()
        mGankViewModel.getCategories(Category.Article.name)
        mGankViewModel.getCategories(Category.GanHuo.name)
        mGankViewModel.getCategories(Category.Girl.name)
    }

    override fun toolbarVisible() = false
}