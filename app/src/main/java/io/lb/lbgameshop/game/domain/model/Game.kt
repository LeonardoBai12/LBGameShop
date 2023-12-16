package io.lb.lbgameshop.game.domain.model

import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Data of game on sale.
 *
 * @property gameID ID of the game.
 * @property title Game's title.
 * @property salePrice Game price provided by CheapShark.
 * @property normalPrice Game price on the original store.
 * @property metacriticScore Game score on Metacritic.
 * @property steamRatingText Text that shows how good a game is acordding to the users.
 * @property steamRatingPercent Ratio of positive ratings.
 * @property steamRatingCount Amount of users that rated this game.
 * @property releaseDate Date this game have released.
 * @property thumb Game cover image.
 */
data class Game(
    val gameID: String,
    val title: String,
    val salePrice: Double,
    val normalPrice: Double,
    val metacriticScore: Int?,
    val steamRatingText: String?,
    val steamRatingPercent: Int?,
    val steamRatingCount: Int?,
    val releaseDate: Long?,
    val thumb: String,
) {
    companion object {
        fun fromJson(json: String): Game = Gson().fromJson(json, Game::class.java)
    }

    fun toJson(): String {
        return URLEncoder.encode(
            Gson().toJson(this).toString(),
            StandardCharsets.UTF_8.toString()
        ).replace("+", " ")
    }
}
