package com.github.theapache64.now.ui.screen.dashboard

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.theapache64.now.data.remote.TIME_URL
import com.github.theapache64.now.data.repo.DateTimeRepo
import com.github.theapache64.now.perf.AppPerfTracer
import com.github.theapache64.now.perf.PerformanceTracer
import com.github.theapache64.now.perf.PerformanceTracer.PagePerfTracer.APP_STARTUP_MARKER
import com.github.theapache64.now.perf.toPerfKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dateTimeRepo: DateTimeRepo,
    private val appPerfTracer: AppPerfTracer,
    private val performanceTracer: PerformanceTracer
) : ViewModel() {
    companion object{
         val TAG = DashboardViewModel::class.java.simpleName
    }
    var dateTime by mutableStateOf<String?>(null)
        private set

    var isPageLoaded by mutableStateOf<Boolean>(false)
        private set

    init {
        refreshTime()
    }

    fun onClickMeClicked() {
        refreshTime()
    }

    private fun refreshTime() {
        viewModelScope.launch {
            performanceTracer.apiTracer.startMarker(TIME_URL)
            dateTime = dateTimeRepo.getCurrentDateTime()
            performanceTracer.apiTracer.endMarker(TIME_URL)
            appPerfTracer.traceDataProcessingEndTime()
            isPageLoaded = true
        }
    }

    fun onPageRendered() {
        val pageRenderTime: Long
        val pageUrl = TIME_URL
        var pageApiResponseTime = performanceTracer.apiTracer.getMeasureTime(pageUrl)
        var startApiResponseTime = pageApiResponseTime
        appPerfTracer.setApiResponseTime(startApiResponseTime)
        val requestId = performanceTracer.apiTracer.getRequestId(pageUrl)
        performanceTracer.apiTracer.clearMarker(pageUrl)
        if (performanceTracer.pagePerfTracer.isStarted(APP_STARTUP_MARKER)) {
            performanceTracer.pagePerfTracer.endMarker(APP_STARTUP_MARKER)
            pageRenderTime = performanceTracer.pagePerfTracer.getMeasureTime(APP_STARTUP_MARKER)
            performanceTracer.pagePerfTracer.clearMarker(APP_STARTUP_MARKER)
            Log.d(TAG, "Api response $pageApiResponseTime && page response time is $pageRenderTime")
        } else {
            performanceTracer.pagePerfTracer.endMarker(pageUrl)
            pageRenderTime = performanceTracer.pagePerfTracer.getMeasureTime(pageUrl)
            performanceTracer.pagePerfTracer.clearMarker(pageUrl)
            Log.d(
                TAG,
                "Api time from Tracer is $pageApiResponseTime \n Page Render time from Tracer is $pageRenderTime"
            )
        }
        val protoParseTime = performanceTracer.pagePerfTracer.getProtoMeasureTime(pageUrl.toPerfKey())
        performanceTracer.pagePerfTracer.clearProtoMarker(pageUrl.toPerfKey())
        val apiPerfData = performanceTracer.apiPerfDataCollector.getApiPerfData(pageUrl.toPerfKey())
        performanceTracer.apiPerfDataCollector.clearApiPerfData(pageUrl.toPerfKey())
        appPerfTracer.sendAppStartUpEvent(pageRenderTime)
        performanceTracer.apiTracer.popRequestId(pageUrl)
    }
}