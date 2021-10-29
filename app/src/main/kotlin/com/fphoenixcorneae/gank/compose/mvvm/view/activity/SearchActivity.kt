package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import com.fphoenixcorneae.ext.dpToPx
import com.fphoenixcorneae.ext.toast
import com.fphoenixcorneae.gank.compose.mvvm.view.SearchScreen
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity
import com.fphoenixcorneae.toolbar.CommonToolbar

/**
 * @desc：搜索
 * @date：2021/10/14 10:14
 */
class SearchActivity : BaseActivity() {

    override fun initView() {
        setRealContent {
            SearchScreen()
        }

        onToolbarUpdate = {
            centerType = CommonToolbar.TYPE_CENTER_SEARCH_VIEW
            centerSearchBgCornerRadius = 20f.dpToPx()
        }
        onToolbarClick = { _, action, extra ->
            when (action) {
                CommonToolbar.TYPE_LEFT_IMAGE_BUTTON -> {
                    onBackPressed()
                }
                CommonToolbar.MotionAction.ACTION_SEARCH_SUBMIT -> {
                    toast(extra)
                }
            }
        }
    }

    override fun initListener() {
    }

    override fun initData() {
    }
}