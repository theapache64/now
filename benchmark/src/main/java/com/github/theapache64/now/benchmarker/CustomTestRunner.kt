package com.github.theapache64.now.benchmarker

import android.app.Application
import android.content.Context
import androidx.benchmark.junit4.AndroidBenchmarkRunner
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidBenchmarkRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}