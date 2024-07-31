package com.github.theapache64.now.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.theapache64.now.ui.screen.dashboard.DashboardScreen
import com.github.theapache64.now.ui.screen.splash.SplashScreen
import com.github.theapache64.now.ui.theme.nowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            nowTheme {
                Surface(modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                }) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    private fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.Splash.route) {

            // Splash
            composable(Screen.Splash.route) {
                SplashScreen(onSplashFinished = {
                    val options =
                        NavOptions.Builder().setPopUpTo(Screen.Splash.route, inclusive = true)
                            .build()
                    navController.navigate(
                        Screen.Dashboard.route, options
                    ) // Move to dashboard
                })
            }

            // Dashboard
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
        }
    }
}
