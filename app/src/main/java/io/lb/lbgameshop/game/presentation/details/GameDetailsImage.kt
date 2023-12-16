package io.lb.lbgameshop.game.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.lb.lbgameshop.game.domain.model.Game

@Composable
fun GameDetailsImage(game: Game) {
    val loading = remember {
        mutableStateOf(true)
    }

    val couldNotLoad = remember {
        mutableStateOf(false)
    }

    if (loading.value) {
        Box(
            modifier = Modifier
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (couldNotLoad.value) {
        Box(
            modifier = Modifier
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Couldn't load picture")
        }
    }

    Image(
        modifier = Modifier
            .heightIn(max = 400.dp)
            .fillMaxWidth(),
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
        contentScale = ContentScale.FillWidth,
        contentDescription = "Game thumbnail",
    )
}
