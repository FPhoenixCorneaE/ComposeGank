package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import androidx.activity.viewModels
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.constant.Constant
import com.fphoenixcorneae.gank.compose.mvvm.view.ThisWeekHottestScreen
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity

/**
 * @desc：本周最热
 * @date：2021/09/30 16:16
 */
class ThisWeekHottestActivity : BaseActivity() {

    private val mGankViewModel by viewModels<GankViewModel>()
    private val mCategory: String by lazy {
        intent?.getStringExtra(Constant.CATEGORY) ?: Category.Article.name
    }
    private val mHotType: String by lazy {
        intent?.getStringExtra(Constant.TYPE) ?: "views"
    }

    override fun initView() {
        setRealContent {
            ThisWeekHottestScreen(
                context = getLocalContext(),
                category = mCategory,
                type = mHotType,
            )
        }

        onToolbarUpdate = {
            // 设置标题栏属性
            centerText = getString(R.string.this_week_hottest)
            centerTextSize = 20f.spToPx()
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        mGankViewModel.getThisWeekHottest(mHotType, mCategory)
    }
}