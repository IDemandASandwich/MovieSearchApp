package com.project.moviesearch.data

import com.project.moviesearch.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

// Api key
private const val API_KEY = BuildConfig.OMDB_API_KEY

data class Rating(
    val Source: String,
    val Value: String
)

data class MovieResponse(
    val Title: String,
    val Year: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Actors: String,
    val Plot: String,
    val Ratings: List<Rating>,
    val Response: String,
    val Error: String
)

interface OmdbApiService{
    @GET("/")
    suspend fun getMovieDetails(
        @Query("t") title: String,
        @Query("apikey") apikey: String = API_KEY
    ): MovieResponse
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    private const val BASE_URL = "https://www.omdbapi.com/"

    @Provides
    @Singleton
    fun provideOmdbApiService(): OmdbApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApiService::class.java)
    }
}
