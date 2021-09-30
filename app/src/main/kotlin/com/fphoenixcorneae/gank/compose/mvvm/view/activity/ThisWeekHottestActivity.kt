package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import android.content.Context
import android.content.Intent
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.gank.compose.R
import com.fphoenixcorneae.gank.compose.mvvm.view.ThisWeekHottestScreen
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity

/**
 * @desc：本周最热
 * @date：2021/09/30 16:16
 */
class ThisWeekHottestActivity : BaseActivity() {

    override fun initView() {
        setRealContent {
            ThisWeekHottestScreen()
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
    }

    companion object {
        private const val CATEGORY = "category"

        fun start(context: Context, category: String?) {
            val intent = Intent(context, ThisWeekHottestActivity::class.java).apply {
                putExtra(CATEGORY, category)
            }
            context.startActivity(intent)
        }
    }
}