package me.cniekirk.core.jellydroid.domain.usecase

import me.cniekirk.core.jellydroid.domain.model.Movie
import javax.inject.Inject

internal class GetMoviesUseCaseImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : GetMoviesUseCase {

    override suspend fun invoke(query: String): List<Movie> {
        TODO("Not yet implemented")
    }
}