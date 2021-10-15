package com.fphoenixcorneae.gank.compose.application

import android.os.Process
import com.fphoenixcorneae.gank.compose.BuildConfig
import com.fphoenixcorneae.gank.compose.constant.Constant
import com.fphoenixcorneae.jetpackmvvm.compose.base.application.BaseApplication
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * @desc：GankApp
 * @date：2021/09/29 14:43
 */
class GankApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // Bugly 初始化
        initBugly()
    }

    /**
     * Bugly 初始化
     * 注意：
     * 1、为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
     * 2、建议在测试阶段建议设置成true，发布时设置为false。
     * 3、如果App使用了多进程且各个进程都会初始化Bugly（例如在Application类onCreate()中初始化Bugly），
     * 那么每个进程下的Bugly都会进行数据上报，造成不必要的资源浪费。因此，为了节省流量、内存等资源，
     * 建议初始化的时候对上报进程进行控制，只在主进程下上报数据：判断是否是主进程（通过进程名是否为包名来判断），
     * 并在初始化Bugly时增加一个上报进程的策略配置。
     */
    private fun initBugly() {
        val context = applicationContext
        // 获取当前包名
        val packageName: String = context.packageName
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 是否在测试阶段
        val isDebug = BuildConfig.DEBUG
        // 初始化 Bugly
        CrashReport.initCrashReport(this, Constant.BuglyAppId, isDebug, strategy)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (processName.isNotEmpty()) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
}