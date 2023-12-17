package io.lb.lbgameshop.game.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.lb.lbgameshop.core.presentation.widgets.DefaultTextButton
import io.lb.lbgameshop.core.util.reviewColor
import io.lb.lbgameshop.core.util.toCurrencyString
import io.lb.lbgameshop.core.util.toReleaseDate
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.presentation.SalePriceText

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun GameDetailsScreen(
    navController: NavHostController,
    game: Game,
    onClickAddToCart: (Game) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            GameDetailsImage(game)

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = game.title,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                SalePriceText(
                    game = game,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row {
                    Text(
                        text = "Original price::",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = game.normalPrice.toCurrencyString(),
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                game.releaseDate?.takeIf {
                    it != 0L
                }?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = "Release date:",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = it.toReleaseDate() ?: "",
                            fontSize = 18.sp,
                        )
                    }
                }

                game.metacriticScore?.takeIf {
                    it != 0
                }?.let {
                    Row {
                        Text(
                            text = "Metacritic score:",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = it.toString(),
                            fontSize = 18.sp,
                        )
                    }
                }

                game.steamRatingPercent?.takeIf {
                    it != 0
                }?.let { steamRatingPercent ->
                    Row {
                        Text(
                            text = "Steam rating:",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$steamRatingPercent%",
                            fontSize = 18.sp,
                        )
                        game.steamRatingCount?.let {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "(${game.steamRatingCount})")
                        }
                    }

                    game.steamRatingText?.let {
                        Text(
                            text = it,
                            color = it.reviewColor(),
                            fontSize = 18.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DefaultTextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    text = "Add to Cart"
                ) {
                    onClickAddToCart.invoke(game)
                    navController.navigateUp()
                }
            }
        }
    }
}
