package com.fphoenixcorneae.gank.compose.mvvm.view.activity

import androidx.activity.viewModels
import com.fphoenixcorneae.ext.spToPx
import com.fphoenixcorneae.gank.compose.constant.Constant
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
        intent?.getStringExtra(Constant.TITLE) ?: ""
    }
    private val mPostId: String by lazy {
        intent?.getStringExtra(Constant.POST_ID) ?: ""
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
}