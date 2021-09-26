package com.fphoenixcorneae.gank.compose.network

import com.fphoenixcorneae.coretrofit.model.BaseResponse
import com.fphoenixcorneae.gank.compose.network.service.GankService

/**
 * @desc：服务器返回数据的基类
 * 如果你的项目中有基类，那美滋滋，可以继承BaseResponse，请求时框架可以帮你自动脱壳，自动判断是否请求成功，怎么做：
 * 1.继承 [BaseResponse]
 * 2.重写 [isSuccess] 方法，编写你的业务需求，根据自己的条件判断数据是否请求成功
 * 3.重写 [getResponseCode]、[getResponseData]、[getResponseMsg] 方法，传入你的 code data msg
 * @date：2021/09/26 16:31
 */
data class ApiResponse<T>(val status: Int, val msg: String?, val data: T) : BaseResponse<T>() {

    /**
     * 这里是示例：干货集中营 网站返回的 status 为 100 就代表请求成功，请根据自己的业务需求来改变
     */
    override fun isSuccess() = status == GankService.REQUEST_OK

    override fun getResponseCode() = status

    override fun getResponseData() = data

    override fun getResponseMsg() = msg ?: ""
}