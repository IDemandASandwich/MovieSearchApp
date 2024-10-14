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

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.project.moviesearch.data.MovieResponse
import com.project.moviesearch.data.OmdbApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import javax.inject.Inject

sealed class MovieState{
    data class Success(val movie: MovieResponse): MovieState()
    data class Error(val message: String): MovieState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiService: OmdbApiService
) : ViewModel() {

    private val _movieState = MutableStateFlow<MovieState?>(null)
    val movieState = _movieState.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun fetchMovieDetails(title: String){
        viewModelScope.launch{
            try {
                val response = apiService.getMovieDetails(title, "bcd53905") //TODO make a var, if no internet gets stuck here
                _movieState.value = MovieState.Success(response)
            } catch (e: HttpException){
                _movieState.value = MovieState.Error("HTTP Error: ${e.message}")
            } catch (e: IOException) {
                _movieState.value = MovieState.Error("Network error. Please check your internet connection.")
            } catch (e: Exception) {
                _movieState.value = MovieState.Error("An unexpected error occurred.")
            }
        }
    }
}