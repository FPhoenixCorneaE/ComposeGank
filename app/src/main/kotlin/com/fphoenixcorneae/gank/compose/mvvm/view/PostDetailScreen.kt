package com.fphoenixcorneae.gank.compose.mvvm.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fphoenixcorneae.gank.compose.mvvm.viewmodel.GankViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.theme.typography

/**
 * 文章详情
 */
@Composable
fun PostDetailScreen() {
    PostDetail()
    PostComments()
}

@Composable
private fun PostDetail(gankViewModel: GankViewModel = viewModel()) {
    val postDetail = gankViewModel.postDetail.collectAsState().value
    Column(
        modifier = Modifier
            .padding(top = 84.dp)
            .fillMaxWidth()
    ) {
        Row {
            AuthorImage()
            AuthorNameAndPublishedTime(
                authorName = postDetail.author ?: "",
                publishedAt = postDetail.publishedAt ?: ""
            )
        }
        Text(
            text = postDetail.desc ?: "",
            style = typography.body1,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, 16.dp)
                .fillMaxWidth()
        )
        Images(images = postDetail.images)
        IconSection(
            watchCount = postDetail.views,
            starCount = postDetail.stars,
            likeCount = postDetail.likeCounts
        )
        Divider(thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
    }
}

/**
 * 文章评论
 */
@Composable
private fun PostComments() {

}