package com.github.theapache64.now.benchmarker

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SampleStartupBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.github.theapache64.now",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.DEFAULT,
        iterations = 5,
        setupBlock = {
            // Press home button before each run to ensure the starting activity isn't visible.
            pressHome()
        }
    ) {
        // starts default launch activity
        startActivityAndWait()

        // Check if composable TAG_DASHBOARD is visible
        val dateTime = By.res("DateTime")
        val isFound = device.wait(Until.hasObject(dateTime), 5000)
        if (!isFound) {
            throw AssertionError("DateTime not found")
        }
    }
}
