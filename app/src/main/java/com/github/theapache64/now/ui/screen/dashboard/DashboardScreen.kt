package com.github.theapache64.now.ui.screen.dashboard

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.theapache64.now.R


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    ReportDrawn()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("DashboardScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Greetings
        viewModel.dateTime?.let { dateTime ->
            Text(
                text = dateTime,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.testTag("DateTime")
            )
        }


        Spacer(modifier = Modifier.height(30.dp))

        // Action : Click Me
        Button(onClick = {
            viewModel.onClickMeClicked()
        }) {
            Text(text = stringResource(id = R.string.action_click_me))
        }
    }
}