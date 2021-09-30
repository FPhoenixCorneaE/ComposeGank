package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.mvvm.view.CategoryListScreen
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity

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
            CategoryListScreen(context = getLocalContext())
        }

        onToolbarUpdate = {
            // 设置标题栏属性
            centerText = mType
            centerTextSize = 20f.spToPx()
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        mGankViewModel.getCategoryList(category = mCategory, type = mType)
    }

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