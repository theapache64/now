package com.github.theapache64.now

import android.app.Application
import com.github.theapache64.now.perf.AppPerfTracer
import com.github.theapache64.now.perf.AppStartUpTimeHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(){

    @Inject
    lateinit var appStartUpTimeHelper: AppStartUpTimeHelper

    override fun onCreate() {
        super.onCreate()
        appStartUpTimeHelper.init(this)
        appStartUpTimeHelper.markAppInitTimestamp()
    }
}