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

package com.project.moviesearch.ui.mainactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.project.moviesearch.data.MainActivityRepository
import com.project.moviesearch.data.MovieResponse
import com.project.moviesearch.data.OmdbApiService
import com.project.moviesearch.ui.mainactivity.MainActivityUiState.Error
import com.project.moviesearch.ui.mainactivity.MainActivityUiState.Loading
import com.project.moviesearch.ui.mainactivity.MainActivityUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiService: OmdbApiService
) : ViewModel() {

    private val _movieState = MutableStateFlow<MovieResponse?>(null)
    val movieState = _movieState.asStateFlow()

    fun fetchMovieDetails(title: String){
        viewModelScope.launch{
            try{
                val response = apiService.getMovieDetails(title, "bcd53905") //TODO make a var
                Log.d("MAIN",response.toString())
                _movieState.value = response
            } catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Error(val throwable: Throwable) : MainActivityUiState
    data class Success(val data: List<String>) : MainActivityUiState
}
