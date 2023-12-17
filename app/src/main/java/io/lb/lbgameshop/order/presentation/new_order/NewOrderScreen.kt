package io.lb.lbgameshop.order.presentation.new_order

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.order.presentation.widgets.OrderDetailsScreen

@ExperimentalMaterial3Api
@Composable
fun NewOrderScreen(
    navController: NavController,
    state: NewOrderState,
    onClickItem: (Game) -> Unit,
    onClickDelete: (Game) -> Unit,
    onRestoreItem: () -> Unit,
    onClickFinish: () -> Unit
) {
    OrderDetailsScreen(
        navController = navController,
        order = state.order,
        items = state.items,
        onClickItem = onClickItem,
        onClickDelete = onClickDelete,
        onRestoreItem = onRestoreItem,
        onClickFinish = onClickFinish,
    )
}
