package com.github.theapache64.now.perf

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import javax.inject.Inject
import javax.inject.Singleton

enum class StartType  {
    START_TYPE_UNSPECIFIED,
    START_TYPE_COLD,
    START_TYPE_WARM,
}

@Singleton
class AppStartUpTimeHelper @Inject constructor() {
    companion object {
        const val APP_START_UP_TYPE_COLD = "cold"
        const val APP_START_UP_TYPE_WARM = "warm"
        const val APP_START_UP_TYPE_HOT = "hot"
    }

    var appStartUpType = ""
    // time taken to draw first frame after user clicks on App Icon
    var appStartUpTime: Long = 0L
    var appStartUpTimeStamp: Long = 0L

    private var appInitTS = 0L
    private var activityCreatedTS = 0L

    private var contentProviderStartTime: Long = StartTimeProvider.providerClassLoadMs
    internal var firstDrawTime: Long = 0L
    internal var hotStartTime: Long = 0L
    internal var isAppStartFlow = false
    internal var firstPostEnqueued = false

    fun init(context: Context) {
        @Suppress("DEPRECATION") val handler = Handler()
        if (isForegroundProcess()) {
            firstPostEnqueued = true
            @Suppress("DEPRECATION")
            handler.post {
                firstPostEnqueued = false
                isAppStartFlow = true
            }
        }
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            AppStartUpLifeCycleCallbacks(this@AppStartUpTimeHelper)
        )
    }

    internal fun setAppStartUpTime() {
        if (appStartUpType == APP_START_UP_TYPE_COLD) {
            appStartUpTime = firstDrawTime - getProcessStartTime()
            // sometimes bind application starts then halts mid way and the actual app start is much later.
            // so adding a 30 sec limit for filtering those out.
            // refer https://dev.to/pyricau/android-vitals-when-did-my-app-start-24p4
            if (appStartUpTime > 30_000L) {
                appStartUpTime = firstDrawTime - contentProviderStartTime
            }
        } else {
            appStartUpTime = firstDrawTime - hotStartTime
        }
    }

     fun getStartUpType(): StartType {
        return when (appStartUpType) {
            APP_START_UP_TYPE_COLD -> {
                StartType.START_TYPE_COLD
            }
            APP_START_UP_TYPE_WARM -> {
                StartType.START_TYPE_WARM
            }
            else -> {
                StartType.START_TYPE_UNSPECIFIED
            }
        }
    }

    private fun getProcessStartTime(): Long {
        val processStartupTime: Long = when {
            Build.VERSION.SDK_INT < 24 -> {
                contentProviderStartTime
            }
            else -> {
                android.os.Process.getStartUptimeMillis()
            }
        }
        return processStartupTime
    }

    private fun isForegroundProcess(): Boolean {
        val processInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(processInfo)
        return processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }

    fun markAppInitTimestamp() {
        appInitTS = SystemClock.uptimeMillis()
    }
    fun getAppInitTimestamp(): Long = appInitTS

    fun getAppInitTime(): Long = appInitTS - getProcessStartTime()

    fun markActivityCreatedTimestamp() {
        activityCreatedTS = SystemClock.uptimeMillis()
    }
    fun getActivityCreatedTimestamp(): Long = activityCreatedTS
}
