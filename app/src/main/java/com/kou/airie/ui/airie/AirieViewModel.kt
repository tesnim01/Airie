/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kou.airie.ui.airie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.kou.airie.data.AirieRepository
import com.kou.airie.ui.airie.AirieUiState.Error
import com.kou.airie.ui.airie.AirieUiState.Loading
import com.kou.airie.ui.airie.AirieUiState.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AirieViewModel @Inject constructor(
    private val airieRepository: AirieRepository
) : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()
    val uiState: StateFlow<AirieUiState> = airieRepository
        .airies.map<List<String>, AirieUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    init {
        viewModelScope.launch {
            delay(2000L)
            _isReady.value = true
        }
    }
    fun addAirie(name: String) {
        viewModelScope.launch {
            airieRepository.add(name)
        }
    }
}

sealed interface AirieUiState {
    object Loading : AirieUiState
    data class Error(val throwable: Throwable) : AirieUiState
    data class Success(val data: List<String>) : AirieUiState
}
