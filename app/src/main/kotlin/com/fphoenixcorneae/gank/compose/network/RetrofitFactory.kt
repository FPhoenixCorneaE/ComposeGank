package com.fphoenixcorneae.gank.compose.network

import android.annotation.SuppressLint
import com.fphoenixcorneae.coretrofit.factory.CoroutineCallAdapterFactory
import com.fphoenixcorneae.coretrofit.interceptor.CommonParamsInterceptor
import com.fphoenixcorneae.coretrofit.interceptor.HeaderInterceptor
import com.fphoenixcorneae.gank.compose.network.interceptor.logging.LogInterceptor
import com.fphoenixcorneae.util.ContextUtil
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @desc：RetrofitHelper
 * @date：2021/09/24 10:34
 */
object RetrofitFactory {

    private const val TIMEOUT_CONNECT = 10L
    private const val TIMEOUT_READ = 10L
    private const val TIMEOUT_WRITE = 10L
    private const val NET_CACHE_DIR = "netCache"
    private const val NET_CACHE_MAX_SIZE = 10 * 1024 * 1024L

    /**
     * 公共请求头
     */
    var headers: Map<String, String?>? = null

    /**
     * 公共请求参数
     */
    var commonParams: Map<String, String?>? = null

    /**
     * 获取 ServiceApi
     */
    fun <T> getApi(
        serviceClass: Class<T>,
        baseUrl: String
    ): T = mRetrofitBuilder.baseUrl(baseUrl).build().create(serviceClass)

    /**
     * 清空 Cookies
     */
    fun clearCookies() {
        mCookieJar.clear()
    }

    /**
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    private fun OkHttpClient.Builder.setHttpClientBuilder(): OkHttpClient.Builder = apply {
        // 添加公共请求头 headers, 注意要设置在日志拦截器之前，不然 Log 中会不显示 header 信息
        addInterceptor(HeaderInterceptor(headers))
        // 添加公共请求参数 commonParams
        addInterceptor(CommonParamsInterceptor(commonParams))
        // 添加缓存拦截器 可传入缓存天数，不传默认7天
//        addInterceptor(CacheInterceptor())
        // 日志拦截器
        addInterceptor(LogInterceptor())
        // 连接池
        connectionPool(ConnectionPool(200, 5, TimeUnit.MINUTES))
        // 错误重连
        retryOnConnectionFailure(true)
        // 设置缓存配置 缓存最大 10M
        cache(Cache(File(ContextUtil.context.cacheDir, NET_CACHE_DIR), NET_CACHE_MAX_SIZE))
        // 添加 Cookies 自动持久化
        cookieJar(mCookieJar)
        // 信任所有证书,不安全有风险
        val trustManager = createTrustManager()
        val sslSocketFactory = createSSLSocketFactory(arrayOf(trustManager))
        sslSocketFactory?.let {
            sslSocketFactory(it, trustManager)
        }
        hostnameVerifier { _, _ -> true }
        // 超时时间 连接、读、写
        connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
    }

    private fun createTrustManager() = kotlin.run {
        @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    private fun createSSLSocketFactory(tm: Array<TrustManager>) = kotlin.run {
        var sslSocketFactory: SSLSocketFactory? = null
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tm, SecureRandom())
            sslSocketFactory = sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sslSocketFactory
    }

    /**
     * 在这里可以对 Retrofit.Builder 做任意操作，比如添加解析器：Gson、Protocol
     */
    private fun Retrofit.Builder.setRetrofitBuilder(): Retrofit.Builder = apply {
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    /**
     * Cookies 自动持久化
     */
    private val mCookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(ContextUtil.context))
    }

    /**
     * OkHttpClient
     */
    private val mOkHttpClient: OkHttpClient by lazy {
        RetrofitUrlManager
            .getInstance()
            .with(OkHttpClient.Builder())
            .setHttpClientBuilder()
            .build()
    }

    /**
     * RetrofitBuilder
     */
    private val mRetrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .client(mOkHttpClient)
            .setRetrofitBuilder()
    }
}



