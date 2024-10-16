package com.project.moviesearch.ui.layout

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.project.moviesearch.R
import com.project.moviesearch.data.MovieResponse
import com.project.moviesearch.ui.SearchBar
import com.project.moviesearch.ui.mainactivity.MovieState

@Composable
internal fun CompactLandscapeLayout(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    movieState: MovieState?
){
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        SearchBar(onClick)

        if(movieState is MovieState.Success){
            MovieDetailsLandscape(movieState.movie)
        }
        else if(movieState is MovieState.Error){
            val errorMessage = movieState.message
            Text(stringResource(R.string.result_error, errorMessage))
        }
    }
}

@Composable
fun MovieDetailsLandscape(movie: MovieResponse) {
    val modifier = Modifier
        .fillMaxWidth()

    val detailsLeft = listOf(
        stringResource(R.string.result_title, movie.Title),
        stringResource(R.string.result_year, movie.Year),
        stringResource(R.string.result_runtime, movie.Runtime),
        stringResource(R.string.result_genre, movie.Genre),
        stringResource(R.string.result_actors, movie.Actors)
    )

    val detailsRight = listOf(
        stringResource(R.string.result_plot, movie.Plot)
    )

    Row{
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            detailsLeft.forEach { detail ->
                MovieCardLandscape(title = detail, modifier = modifier)
            }
        }
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            detailsRight.forEach { detail ->
                MovieCardLandscape(title = detail, modifier = modifier)
            }

            Card(
                modifier = modifier
            ){
                movie.Ratings.forEach { rating ->
                    Text(
                        text = stringResource(R.string.result_source_rating, rating.Source, rating.Value),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
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