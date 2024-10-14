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

import android.graphics.Movie
import android.os.Build
import androidx.annotation.RequiresExtension
import com.project.moviesearch.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.moviesearch.R
import com.project.moviesearch.data.MovieResponse

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val movieState by viewModel.movieState.collectAsState()

    MainActivityScreen(
        modifier = modifier,
        onClick = { viewModel.fetchMovieDetails(it) },
        movieState = movieState
    )
}

@Composable
internal fun MainActivityScreen(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    movieState: MovieState?
) {
    Column(modifier) {
        var movieTitle by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                label = {Text(stringResource(R.string.movie_title))},
                value = movieTitle,
                onValueChange = { movieTitle = it }
            )

            Button(
                modifier = Modifier.width(96.dp),
                onClick = { onClick(movieTitle) }
            ) {
                Text(stringResource(R.string.search))
            }
        }

        if(movieState is MovieState.Success){
            val movie = movieState.movie
            Column{
                Text("Title: ${movie.Title}")
                Text("Year: ${movie.Year}")
                Text("Runtime: ${movie.Runtime}")
                Text("Genre: ${movie.Genre}")
                Text("Actors: ${movie.Actors}")
                Text("Plot: ${movie.Plot}")
            }
        }
        else if(movieState is MovieState.Error){
            val errorMessage = movieState.message
            Text("Error: $errorMessage")
        }
    }
}

// Previews

//@Preview(showBackground = true)
//@Composable
//private fun DefaultPreview() {
//    MyApplicationTheme {
//        MainActivityScreen(Modifier, {}, )
//    }
//}
//
//@Preview(showBackground = true, widthDp = 480)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        MainActivityScreen(Modifier, {}, )
//    }
//}
