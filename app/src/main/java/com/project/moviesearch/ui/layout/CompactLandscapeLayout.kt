package com.project.moviesearch.ui.layout

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.project.moviesearch.R
import com.project.moviesearch.ui.SearchBar
import com.project.moviesearch.ui.mainactivity.MovieState

@Composable
internal fun CompactLandscapeLayout(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    movieState: MovieState?
){
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .fillMaxSize()
    ) {
        // Left part
        Column(
            modifier = Modifier
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_search_bottom)),
                onClick = onClick
            )

            if (movieState is MovieState.Success) {
                val movie = movieState.movie

                val detailsLeft = listOf(
                    stringResource(R.string.result_title, movie.Title),
                    stringResource(R.string.result_year, movie.Year),
                    stringResource(R.string.result_runtime, movie.Runtime),
                    stringResource(R.string.result_genre, movie.Genre),
                )

                detailsLeft.forEach { detail ->
                    MovieCardLandscape(title = detail, modifier = Modifier.fillMaxWidth())
                }
            }
            else if (movieState is MovieState.Error) {
                val errorMessage = movieState.message
                Text(stringResource(R.string.result_error, errorMessage))
            }
        }

        // Right part
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (movieState is MovieState.Success) {
                val movie = movieState.movie

                val detailsRight = listOf(
                    stringResource(R.string.result_actors, movie.Actors),
                    stringResource(R.string.result_plot, movie.Plot)
                )

                detailsRight.forEach { detail ->
                    MovieCardLandscape(title = detail, modifier = Modifier.fillMaxWidth())
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    movie.Ratings.forEach { rating ->
                        Text(
                            text = stringResource(
                                R.string.result_source_rating,
                                rating.Source,
                                rating.Value
                            ),
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCardLandscape(title: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}