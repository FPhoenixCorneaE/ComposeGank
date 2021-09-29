package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import androidx.activity.viewModels
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.ext.view.gone
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.view.HomepageScreen
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
            HomepageScreen(context = getLocalContext())
        }

        onToolbarUpdate = {
            leftImageButton.gone()
            centerText = context.getString(R.string.app_name)
            centerTextSize = 20f.spToPx()
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        mGankViewModel.getHomepageBanners()
        mGankViewModel.getCategories(Category.Article.name)
        mGankViewModel.getCategories(Category.GanHuo.name)
        mGankViewModel.getCategories(Category.Girl.name)
    }
}