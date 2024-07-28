package com.github.theapache64.now.perf

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import com.github.theapache64.now.perf.NextDrawListener.Companion.onNextDraw
import javax.inject.Inject

class AppStartUpLifeCycleCallbacks @Inject constructor(private val appStartUpTimeHelper: AppStartUpTimeHelper) :
    Application.ActivityLifecycleCallbacks {
    @Suppress("DEPRECATION")
    val handler = Handler()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        var firstDraw = false
        appStartUpTimeHelper.hotStartTime = SystemClock.uptimeMillis()
        if (appStartUpTimeHelper.firstPostEnqueued && savedInstanceState == null) {
            appStartUpTimeHelper.appStartUpType = AppStartUpTimeHelper.APP_START_UP_TYPE_COLD
        } else {
            appStartUpTimeHelper.appStartUpType = AppStartUpTimeHelper.APP_START_UP_TYPE_WARM
            appStartUpTimeHelper.isAppStartFlow = true
        }
        if (firstDraw) {
            return
        }
        val window = activity.window
        window?.decorView?.onNextDraw {
            if (firstDraw) return@onNextDraw
            firstDraw = true
            appStartUpTimeHelper.firstDrawTime = SystemClock.uptimeMillis()
            appStartUpTimeHelper.setAppStartUpTime()
        }
        appStartUpTimeHelper.markActivityCreatedTimestamp()
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, savedInstanceState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
