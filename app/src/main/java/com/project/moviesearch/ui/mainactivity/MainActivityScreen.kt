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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.moviesearch.R
import com.project.moviesearch.data.MovieResponse
import com.project.moviesearch.data.Rating

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
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                label = {Text(stringResource(R.string.movie_title))},
                value = movieTitle,
                onValueChange = { movieTitle = it },
                singleLine = true,
            )

            Button(
                modifier = Modifier
                    .padding(8.dp),
                onClick = { onClick(movieTitle) }
            ) {
                Text(
                    text = stringResource(R.string.search),
                    maxLines = 1
                )
            }
        }

        if(movieState is MovieState.Success){
            val movie = movieState.movie
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ){
                Text(stringResource(R.string.result_title, movie.Title))
                Text(stringResource(R.string.result_year, movie.Year))
                Text(stringResource(R.string.result_runtime, movie.Runtime))
                Text(stringResource(R.string.result_genre, movie.Genre))
                Text(stringResource(R.string.result_actors, movie.Actors))
                Text(stringResource(R.string.result_plot, movie.Plot))
            }
            movie.Ratings.forEach { rating ->
                Text(
                    text = stringResource(R.string.result_source_rating, rating.Source, rating.Value)
                )
            }
        }
        else if(movieState is MovieState.Error){
            val errorMessage = movieState.message
            Text(stringResource(R.string.result_error, errorMessage))
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
    MyApplicationTheme {
        MainActivityScreen(
            modifier = Modifier,
            onClick = {},
            movieState = mockSuccessState
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MainActivityScreen(
            modifier = Modifier,
            onClick = {},
            movieState = mockSuccessState
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
            movieState = mockErrorState // Provide mock error state
        )
    }
}
