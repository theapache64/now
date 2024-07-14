package com.github.theapache64.now.benchmarker

import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import com.github.theapache64.now.di.UrlModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@UninstallModules(UrlModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SampleStartupBenchmark {

    private val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.github.theapache64.now",
        metrics = listOf(StartupTimingMetric()),
        startupMode = StartupMode.COLD,
        iterations = 3,
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
