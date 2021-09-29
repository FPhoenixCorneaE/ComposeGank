package com.fphoenixcorneae.gank.compose.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fphoenixcorneae.coretrofit.exception.Error
import com.fphoenixcorneae.ext.loge
import com.fphoenixcorneae.gank.compose.constant.Category
import com.fphoenixcorneae.gank.compose.ext.request
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryBean
import com.fphoenixcorneae.gank.compose.mvvm.model.CategoryListBean
import com.fphoenixcorneae.gank.compose.mvvm.model.HomepageBannersBean
import com.fphoenixcorneae.gank.compose.network.RetrofitFactory
import com.fphoenixcorneae.gank.compose.network.service.GankService
import com.fphoenixcorneae.gank.compose.paging.CategoryListPagingSource
import com.fphoenixcorneae.jetpackmvvm.compose.base.viewmodel.BaseViewModel
import com.fphoenixcorneae.jetpackmvvm.compose.uistate.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @desc：GankViewModel
 * @date：2021/09/23 15:35
 */
class GankViewModel : BaseViewModel() {

    /** GankService */
    private val mGankService by lazy {
        RetrofitFactory.getApi(GankService::class.java, GankService.BASE_URL)
    }

    /** UiState */
    private val _uiState by lazy { MutableStateFlow<UiState>(UiState.ShowLoading()) }
    val uiState = _uiState.asStateFlow()

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

    /** 分类列表数据 */
    private var _categoryList: Flow<PagingData<CategoryListBean.Data>>? = null
    val categoryList
        get() = _categoryList

    /**
     * 获取首页 banner 轮播
     */
    fun getHomepageBanners() {
        request({
            mGankService.getHomepageBanners()
        }, {
            _homepageBanners.value = it.toMutableList()
        }, {
            "getHomepageBanners: errCode: ${it.errCode} errorMsg: ${it.errorMsg}".loge()
        })
    }

    /**
     * 获取分类
     */
    fun getCategories(category: String) {
        request({
            mGankService.getCategories(category = category)
        }, {
            when (category) {
                Category.Article.name -> _articleCategories.value = it.toMutableList()
                Category.GanHuo.name -> _ganHuoCategories.value = it.toMutableList()
                Category.Girl.name -> _girlCategories.value = it.toMutableList()
            }
            _uiState.value = UiState.ShowContent
        }, {
            "getCategories: errCode: ${it.errCode} errorMsg: ${it.errorMsg}".loge()
            if (it.errCode == Error.NETWORK_ERROR.getCode() || it.errCode == Error.TIMEOUT_ERROR.getCode()) {
                _uiState.value = UiState.ShowNoNetwork(noNetworkMsg = it.errorMsg)
            } else {
                _uiState.value = UiState.ShowError(errorMsg = it.errorMsg)
            }
        })
    }

    /**
     * 获取分类列表数据
     */
    fun getCategoryList(category: String, type: String, count: Int = 10) {
        viewModelScope.launch {
            delay(1_500)
            _uiState.value = UiState.ShowContent
        }
        _categoryList = Pager(PagingConfig(pageSize = count)) {
            CategoryListPagingSource(gankService = mGankService, category = category, type = type, count = count)
        }.flow.cachedIn(viewModelScope)
    }
}