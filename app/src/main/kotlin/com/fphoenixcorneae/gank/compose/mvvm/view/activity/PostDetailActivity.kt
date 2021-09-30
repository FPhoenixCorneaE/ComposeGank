package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.gank.compose.mvvm.view.PostDetailScreen
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.base.activity.BaseActivity

/**
 * @desc：文章详情
 * @date：2021/09/30 10:42
 */
class PostDetailActivity : BaseActivity() {

    private val mGankViewModel by viewModels<GankViewModel>()
    private val mTitle: String by lazy {
        intent?.getStringExtra(TITLE) ?: ""
    }
    private val mPostId: String by lazy {
        intent?.getStringExtra(POST_ID) ?: ""
    }

    override fun initView() {
        setRealContent {
            PostDetailScreen()
        }

        onToolbarUpdate = {
            // 设置标题栏属性
            centerText = mTitle
            centerTextSize = 20f.spToPx()
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        mGankViewModel.getPostDetail(postId = mPostId)
        mGankViewModel.getPostComments(postId = mPostId)
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val TITLE = "title"

        fun start(context: Context, postId: String?, title: String?) {
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra(POST_ID, postId)
                putExtra(TITLE, title)
            }
            context.startActivity(intent)
        }
    }
}