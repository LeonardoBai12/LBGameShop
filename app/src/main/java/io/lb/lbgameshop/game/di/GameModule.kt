package io.lb.lbgameshop.game.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.lb.lbgameshop.game.data.remote.GameClient
import io.lb.lbgameshop.game.data.repository.GameRepositoryImpl
import io.lb.lbgameshop.game.domain.repostitory.GameRepository
import io.lb.lbgameshop.game.domain.use_cases.GameUseCases
import io.lb.lbgameshop.game.domain.use_cases.GetGamesUseCase
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object GameModule {
    @Provides
    fun providesGameClient(retrofit: Retrofit): GameClient {
        return retrofit.create(GameClient::class.java)
    }

    @Provides
    fun providesGameRepository(client: GameClient): GameRepository {
        return GameRepositoryImpl(client)
    }

    @Provides
    fun providesGameUseCases(repository: GameRepository): GameUseCases {
        return GameUseCases(
            getGamesUseCase = GetGamesUseCase(repository)
        )
    }
}
