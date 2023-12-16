package io.lb.lbgameshop.game.data.repository

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.game.data.remote.GameClient
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.domain.repostitory.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GameRepositoryImpl(
    private val client: GameClient
) : GameRepository {
    override fun getGames(): Flow<Resource<List<Game>>> {
        return flow {
            emit(Resource.Loading(true))
            delay(1000)

            val games = try {
                client.getGames().body()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            games?.takeIf {
                it.isNotEmpty()
            }?.let {
                emit(Resource.Success(data = it))
            } ?: emit(Resource.Error("Couldn't load data"))

            emit(Resource.Loading(false))
        }
    }
}
