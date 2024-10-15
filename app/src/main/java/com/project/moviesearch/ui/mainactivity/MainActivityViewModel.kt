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

import retrofit2.HttpException
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

    fun fetchMovieDetails(title: String){
        viewModelScope.launch{
            try {
                val response = apiService.getMovieDetails(title)
                if (response.Response == "False") {
                    _movieState.value = MovieState.Error(response.Error)
                } else {
                    _movieState.value = MovieState.Success(response)
                }
            } catch (e: IOException) {
                _movieState.value =
                    MovieState.Error("Network error. Please check your internet connection.")
            } catch(e: HttpException){
                when(e.code()){
                    401 -> {
                        _movieState.value = MovieState.Error("Invalid API key. Please provide a valid key.")
                    }
                    else -> {
                        _movieState.value = MovieState.Error("HTTP error: ${e.code()}")
                    }
                }
            } catch (e: Exception) {
                _movieState.value = MovieState.Error("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }
}