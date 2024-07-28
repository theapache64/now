package com.github.theapache64.now.perf

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import okio.ByteString
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPerfTracer @Inject constructor(
    private val appStartUpTimeHelper: AppStartUpTimeHelper,
) {
    companion object {
        const val TAG = "AppPerfTracer"
    }


    private var splashPageStartTime = 0L
    private var splashPageEndTime = 0L
    private var dataProcessingStartTime = 0L
    private var dataProcessingEndTime = 0L
    private var apiResponseTime = 0L
    private var splashAnimationStartTime = 0L
    private var splashAnimationEndTime = 0L
    private var fingerprintKeyFetchTime = 0L
    private var processingTimeBeforeStartApi = 0L
    private var processingTimeAfterStartApi = 0L

    private var mandatoryTaskTime_Start = 0L
    private var mandatoryTaskTime_End = 0L
    private var mandatoryTaskTime = 0L

    private var mandatoryTaskTimeInSplash_Start = 0L
    private var mandatoryTaskTimeInSplash = 0L

    private var startUpInitializerTime_Start = 0L
    private var startUpInitializerTime_End = 0L
    private var startUpInitializerInitialiseTime = 0L

    private var mainActivityOpsTime = 0L
    private var mainActivityOpsTime_Start = 0L
    private var mainActivityOpsTime_End = 0L

    private var splashInitStart = 0L

    private var startProtoParseStart_TS = 0L
    private var startProtoParseEnd_TS = 0L

    private var startApiDnsConnectionTime = 0L
    private var startApiSocketConnectionAcquiredTime = 0L
    private var startApiSecureConnectionTime = 0L
    private var startApiResponseBodyTime = 0L
    private var startApiResponseBodyBytes = 0L
    private var startApiCallTimeEventListener = 0L
    private var startApiConnectionAcquiredTime = 0L
    private var startApiRequestHeaderTime = 0L
    private var startApiRequestBodyTime = 0L
    private var startApiResponseHeaderTime = 0L

    private var startApiResponseTimeFromResponseInterceptor = 0L
    private var processingTimeOfResponseInterceptor = 0L
    private var runBlockingTimeResponseInterceptor = 0L

    private var dataProcessingStartToMandatoryStart = 0L
    private var splashViewedAnalyticsTime_Start = 0L
    private var splashViewedAnalyticsTime_End = 0L
    private var splashViewedAnalyticsTime = 0L
    private var apiCallReceivedDataProcessingTime = 0L
    private var navigationFromSplashTime = 0L
    private var retryCountStartApi = 0L

    private var isStartEventSentForLaunch = AtomicBoolean(false)
    private var currentPageUrl: String = ""
    private var currentPageValue: ByteString = ByteString.EMPTY

    @SuppressLint("LogNotTimber")
    fun sendAppStartUpEvent(pageRenderedTime: Long) {
        if (!appStartUpTimeHelper.isAppStartFlow) return
        val splashTime = splashPageEndTime - splashPageStartTime
        val splashAnimationTime = splashAnimationEndTime - splashAnimationStartTime
        val dataProcessingTime = dataProcessingEndTime - dataProcessingStartTime
        val apiResponseTime = apiResponseTime
        val appStartUpTime =
            appStartUpTimeHelper.appStartUpTime + dataProcessingTime + pageRenderedTime
        val loadTime = appStartUpTimeHelper.appStartUpTime + splashTime + pageRenderedTime

        navigationFromSplashTime = dataProcessingEndTime - apiCallReceivedDataProcessingTime
        mainActivityOpsTime = mainActivityOpsTime_End - mainActivityOpsTime_Start
        mandatoryTaskTime = mandatoryTaskTime_End - mandatoryTaskTime_Start
        splashViewedAnalyticsTime = splashViewedAnalyticsTime_End - splashViewedAnalyticsTime_Start
        startUpInitializerInitialiseTime = startUpInitializerTime_End - startUpInitializerTime_Start
        mandatoryTaskTimeInSplash = startUpInitializerTime_End - mandatoryTaskTimeInSplash_Start


        val perfLog = "startup type is: ${appStartUpTimeHelper.appStartUpType} \n" +
                "splash time is: $splashTime \n" +
                "startup time is: ${appStartUpTimeHelper.appStartUpTime} \n" +
                "page Rendered Time is:  $pageRenderedTime \n" +
                "data processing Time is: $dataProcessingTime \n" +
                "api response time is: $apiResponseTime \n" +
                "total time is : $loadTime \n" +
                "total app startup time is: $appStartUpTime"
        Log.i("AppPerfTracer", perfLog)
        appStartUpTimeHelper.isAppStartFlow = false
    }


    fun setApiResponseTime(time: Long) {
        apiResponseTime = time
    }

    fun traceDataProcessingStartTime() {
        dataProcessingStartTime = SystemClock.uptimeMillis()
    }

    fun traceDataProcessingEndTime() {
        dataProcessingEndTime = SystemClock.uptimeMillis()
    }

    fun traceSplashAnimationStartTime() {
        splashAnimationStartTime = SystemClock.uptimeMillis()
    }

    fun traceSplashAnimationEndTime() {
        splashAnimationEndTime = SystemClock.uptimeMillis()
    }

    fun traceSplashPageStartTime() {
        splashPageStartTime = SystemClock.uptimeMillis()
    }

    fun traceSplashPageEndTime() {
        splashPageEndTime = SystemClock.uptimeMillis()
    }

    fun stopAppStartupFlow() {
        appStartUpTimeHelper.isAppStartFlow = false
    }

    fun setFingerprintKeyFetchTime(time: Long) {
        fingerprintKeyFetchTime = time
    }

    fun traceStartApiDnsConnectionTime(time: Long) {
        startApiDnsConnectionTime = time
    }

    fun traceStartApiSocketConnectionAcquiredTime(time: Long) {
        startApiSocketConnectionAcquiredTime = time
    }

    fun traceStartApiSecureConnectionTime(time: Long) {
        startApiSecureConnectionTime = time
    }

    fun traceStartApiResponseBodyTime(time: Long) {
        startApiResponseBodyTime = time
    }

    fun traceStartApiResponseBodyBytes(bytes: Long) {
        startApiResponseBodyBytes = bytes / 1000
    }

    fun traceStartApiCallTime(time: Long) {
        startApiCallTimeEventListener = time
    }

    fun setBeforeApiCallProcessingTime(time: Long) {
        processingTimeBeforeStartApi = time
    }

    fun setAfterApiCallProcessingTime(time: Long) {
        processingTimeAfterStartApi = time
    }

    fun traceMandatoryTaskStartTime() {
        mandatoryTaskTime_Start = SystemClock.uptimeMillis()
    }

    fun traceMandatoryTaskEndTime() {
        mandatoryTaskTime_End = SystemClock.uptimeMillis()
    }

    fun setStartApiResponseTimeFromInterceptor(time: Long) {
        startApiResponseTimeFromResponseInterceptor = time
    }

    fun setProcessingTimeResponseInterceptor(time: Long) {
        processingTimeOfResponseInterceptor = time
    }

    fun setRunBlockingTimeResponseInterceptor(time: Long) {
        runBlockingTimeResponseInterceptor = time
    }

    fun markStartProtoParseStart() {
        startProtoParseStart_TS = SystemClock.uptimeMillis()
    }

    fun markStartProtoParseEnd() {
        startProtoParseEnd_TS = SystemClock.uptimeMillis()
    }

    fun traceStartApiConnectionAcquiredTime(time: Long) {
        startApiConnectionAcquiredTime = time
    }

    fun traceStartUpInitializerInitialiseStartTime() {
        startUpInitializerTime_Start = SystemClock.uptimeMillis()
    }

    fun traceStartUpInitializerInitialiseEndTime() {
        startUpInitializerTime_End = SystemClock.uptimeMillis()
    }

    fun traceMainActivityOpsStartTime() {
        mainActivityOpsTime_Start = SystemClock.uptimeMillis()
    }

    fun traceMainActivityOpsEndTime() {
        mainActivityOpsTime_End = SystemClock.uptimeMillis()
    }

    fun traceSplashInitStart() {
        splashInitStart = SystemClock.uptimeMillis()
    }

    fun traceStartApiRequestHeaderTime(time: Long) {
        startApiRequestHeaderTime = time
    }

    fun traceStartApiRequestBodyTime(time: Long) {
        startApiRequestBodyTime = time
    }

    fun traceStartApiResponseHeaderTime(time: Long) {
        startApiResponseHeaderTime = time
    }

    fun traceDataProcessingStartToMandatoryStart() {
        dataProcessingStartToMandatoryStart = SystemClock.uptimeMillis() - dataProcessingStartTime
        mandatoryTaskTimeInSplash_Start = SystemClock.uptimeMillis()
    }

    fun traceSplashViewedAnalyticsStartTime() {
        splashViewedAnalyticsTime_Start = SystemClock.uptimeMillis()
    }

    fun traceSplashViewedAnalyticsEndTime() {
        splashViewedAnalyticsTime_End = SystemClock.uptimeMillis()
    }

    fun traceResponseReceivedInDataProcessingTime() {
        apiCallReceivedDataProcessingTime = SystemClock.uptimeMillis()
    }

    fun startApiRetryCount(count: Int) {
        retryCountStartApi = count.toLong()
    }

    fun isStartEventSentForLaunch(): Boolean {
        return isStartEventSentForLaunch.get()
    }

    fun getAppCloseTime(): Long {
        isStartEventSentForLaunch.set(true)
        return SystemClock.uptimeMillis() - appStartUpTimeHelper.appStartUpTimeStamp
    }

    fun getCurrentPageUrl(): String {
        return currentPageUrl
    }

    fun getCurrentPageValue(): ByteString {
        return currentPageValue
    }

    fun setCurrentPageInfo(url: String, value: ByteString) {
        currentPageUrl = url
        currentPageValue = value
    }
}
