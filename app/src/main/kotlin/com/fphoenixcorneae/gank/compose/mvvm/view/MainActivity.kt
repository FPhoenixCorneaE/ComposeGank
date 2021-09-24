package com.fphoenixcorneae.gank.compose.mvvm.view

import androidx.activity.viewModels
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity

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
                gankViewModel = mGankViewModel
            )
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        mGankViewModel.getHomepageBanners()
    }

    override fun toolbarVisible() = false
}