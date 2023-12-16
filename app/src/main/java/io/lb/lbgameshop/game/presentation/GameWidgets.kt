package io.lb.lbgameshop.game.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.lbgameshop.core.util.toCurrencyString
import io.lb.lbgameshop.game.domain.model.Game

@Composable
fun SalePriceText(
    modifier: Modifier = Modifier,
    game: Game,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.End,
    color: Color = MaterialTheme.colorScheme.primary
) {
    if (game.salePrice == 0.0) {
        Text(
            modifier = modifier,
            text = "Free",
            color = Color.Green,
            textAlign = textAlign,
            fontSize = fontSize,
            fontWeight = fontWeight,
        )
    } else {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            text = game.salePrice.toCurrencyString(),
            color = color,
            textAlign = textAlign,
            fontWeight = fontWeight,
            fontSize = fontSize,
        )
    }
}
