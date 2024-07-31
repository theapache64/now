package com.github.theapache64.now.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.theapache64.now.BuildConfig
import com.github.theapache64.now.perf.AppPerfTracer
import com.github.theapache64.now.perf.PerformanceTracer
import com.github.theapache64.now.perf.PerformanceTracer.PagePerfTracer.APP_STARTUP_MARKER
import com.github.theapache64.now.util.flow.mutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appPerfTracer: AppPerfTracer,
    private val performanceTracer: PerformanceTracer,
) : ViewModel() {

    companion object {
        private const val SPLASH_DURATION_IN_MILLIS = 1500L
    }

    private val _versionName = MutableStateFlow("v${BuildConfig.VERSION_NAME}")
    val versionName: StateFlow<String> = _versionName

    private val _isSplashFinished = mutableEventFlow<Boolean>()
    val isSplashFinished: SharedFlow<Boolean> = _isSplashFinished

    init {
        appPerfTracer.traceSplashPageStartTime()
        appPerfTracer.traceDataProcessingStartTime()
        viewModelScope.launch {
            delay(SPLASH_DURATION_IN_MILLIS)
            appPerfTracer.traceSplashPageEndTime()
            performanceTracer.pagePerfTracer.startMarker(APP_STARTUP_MARKER)
            _isSplashFinished.emit(true)
        }
    }
}