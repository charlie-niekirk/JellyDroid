package me.cniekirk.core.jellydroid.domain.usecase

import me.cniekirk.core.jellydroid.domain.model.Movie

interface GetMoviesUseCase {

    suspend operator fun invoke(query: String): List<Movie>
}