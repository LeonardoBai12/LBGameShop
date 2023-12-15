package io.lb.lbgameshop.order.presentation.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun OrderDetailsScreen() {
    Scaffold { padding ->
        Text(text = "", modifier = Modifier.padding(padding))
    }
}
