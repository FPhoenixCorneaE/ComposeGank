package com.fphoenixcorneae.gank.compose.mvvm.viewmodel

import com.fphoenixcorneae.ext.loge
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.ext.launch
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.model.HomepageBannersBean
import com.fphoenixcorneae.gank.compose.network.RetrofitFactory
import com.fphoenixcorneae.gank.compose.network.service.GankService
import com.fphoenixcorneae.jetpackmvvm.compose.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @desc：GankViewModel
 * @date：2021/09/23 15:35
 */
class GankViewModel : BaseViewModel() {

    /** GankService */
    private val mGankService by lazy {
        RetrofitFactory.getApi(GankService::class.java, GankService.BASE_URL)
    }

    /** 首页 banner 轮播 */
    private val _homepageBanners = MutableStateFlow(mutableListOf<HomepageBannersBean.Data?>())
    val homepageBanners = _homepageBanners.asStateFlow()

    /** 分类 */
    private val _articleCategories = MutableStateFlow(mutableListOf<CategoryBean.Data?>())
    val articleCategories = _articleCategories.asStateFlow()
    private val _ganHuoCategories = MutableStateFlow(mutableListOf<CategoryBean.Data?>())
    val ganHuoCategories = _ganHuoCategories.asStateFlow()
    private val _girlCategories = MutableStateFlow(mutableListOf<CategoryBean.Data?>())
    val girlCategories = _girlCategories.asStateFlow()

    /**
     * 获取首页 banner 轮播
     */
    fun getHomepageBanners() {
        launch({
            mGankService.getHomepageBanners()
        }, {
            it.data?.let {
                _homepageBanners.value = it.toMutableList()
            }
        }, {
            "getHomepageBanners: $it".loge()
        })
    }

    /**
     * 获取分类
     */
    fun getCategories(categoryType: String) {
        launch({
            mGankService.getCategories(categoryType = categoryType)
        }, {
            it.data?.let {
                when (categoryType) {
                    Category.Article.name -> _articleCategories.value = it.toMutableList()
                    Category.GanHuo.name -> _ganHuoCategories.value = it.toMutableList()
                    Category.Girl.name -> _girlCategories.value = it.toMutableList()
                }
            }
        }, {
            "getCategories: $it".loge()
        })
    }
}