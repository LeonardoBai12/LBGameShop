package io.lb.lbgameshop.game.presentation.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import io.lb.lbgameshop.core.util.shimmerAnimation
import io.lb.lbgameshop.core.util.toCurrencyString
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.presentation.SalePriceText

@ExperimentalMaterial3Api
@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val loading = remember {
        mutableStateOf(true)
    }

    val couldNotLoad = remember {
        mutableStateOf(false)
    }

    if (loading.value) {
        Box(
            modifier = Modifier
                .height(170.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (couldNotLoad.value) {
        Box(
            modifier = Modifier
                .height(170.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Couldn't load picture")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onClick.invoke()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(175.dp)
                .height(160.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = game.thumb,
                    onSuccess = {
                        loading.value = false
                    },
                    onError = {
                        loading.value = false
                        couldNotLoad.value = true
                    }
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Game thumbnail",
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = game.normalPrice.toCurrencyString(),
                fontSize = 12.sp,
                textDecoration = TextDecoration.LineThrough
            )

            SalePriceText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                game = game
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun GameShimmerCard() {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize(0.8F)
                .padding(bottom = 12.dp)
                .shimmerAnimation()
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.7f)
                .shimmerAnimation()
        )
    }
}
