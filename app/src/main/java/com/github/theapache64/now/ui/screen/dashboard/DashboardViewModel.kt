package com.github.theapache64.now.ui.screen.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.theapache64.now.data.repo.DateTimeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dateTimeRepo: DateTimeRepo,
) : ViewModel() {
    var dateTime by mutableStateOf<String?>(null)
        private set

    init {
        refreshTime()
    }

    fun onClickMeClicked() {
        refreshTime()
    }

    private fun refreshTime() {
        viewModelScope.launch {
            dateTime = "Loading... \uD83D\uDCE1"
            dateTime = dateTimeRepo.getCurrentDateTime()
        }
    }
}