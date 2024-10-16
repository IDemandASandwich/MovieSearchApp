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

import com.project.moviesearch.ui.theme.MyApplicationTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.moviesearch.data.MovieResponse
import com.project.moviesearch.data.Rating
import com.project.moviesearch.ui.layout.CompactLandscapeLayout
import com.project.moviesearch.ui.layout.CompactPortraitLayout

@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel = hiltViewModel(),
    windowSize: WindowWidthSizeClass
) {
    val movieState by viewModel.movieState.collectAsState()

    MainActivityScreen(
        modifier = modifier,
        onClick = { viewModel.fetchMovieDetails(it) },
        movieState = movieState,
        windowSize = windowSize
    )
}

@Composable
internal fun MainActivityScreen(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    movieState: MovieState?,
    windowSize: WindowWidthSizeClass
) {
    when(windowSize){
        WindowWidthSizeClass.Compact -> {
            CompactPortraitLayout(modifier, onClick, movieState)
        }
        else -> {
            CompactLandscapeLayout(modifier, onClick, movieState)
        }
    }
}

// Previews
// Dummy values
private val mockRatings = listOf(
    Rating(Source = "Internet Movie Database", Value = "8.7/10"),
    Rating(Source = "Rotten Tomatoes", Value = "87%"),
    Rating(Source = "Metacritic", Value = "73/100")
)

private val mockMovie = MovieResponse(
    Title = "The Matrix",
    Year = "1999",
    Runtime = "136 min",
    Genre = "Action, Sci-Fi",
    Director = "Lana Wachowski, Lilly Wachowski",
    Actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
    Plot = "A computer hacker learns about the true nature of reality and his role in the war against its controllers.",
    Ratings = mockRatings,
    Response = "Success",
    Error = ""
)

private val mockSuccessState = MovieState.Success(movie = mockMovie)
private val mockErrorState = MovieState.Error("An Error occurred!")
// Dummy values

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme{
        MainActivityScreen(
            modifier = Modifier,
            onClick = {},
            movieState = mockSuccessState,
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showBackground = true, widthDp = 915, heightDp = 412, name = "Landscape preview")
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MainActivityScreen(
            modifier = Modifier,
            onClick = {},
            movieState = mockSuccessState,
            windowSize = WindowWidthSizeClass.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    MyApplicationTheme {
        MainActivityScreen(
            modifier = Modifier,
            onClick = {},
            movieState = mockErrorState,
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}
