package io.lb.lbgameshop.order.presentation.widgets

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import io.lb.lbgameshop.core.presentation.widgets.SwipeableCard
import io.lb.lbgameshop.core.util.toCurrencyString
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.presentation.SalePriceText

@ExperimentalMaterial3Api
@Composable
fun OrderItemCard(
    game: Game,
    onClickDelete: (() -> Unit)? = null,
    onClickCard: ((Game) -> Unit)? = null,
) {
    SwipeableCard(
        onClickSwiped = onClickDelete,
        swipedContent = {
            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        onClickCard = {
            onClickCard?.invoke(game)
        }
    ) {
        OrderItemCardContent(game)
    }
}

@Composable
private fun OrderItemCardContent(game: Game) {
    Column {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(80.dp)
                    .height(80.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = game.thumb,
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "Game thumbnail",
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = game.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

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
            }
        }
    }
}
