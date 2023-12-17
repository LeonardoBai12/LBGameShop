package io.lb.lbgameshop.order.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.lb.lbgameshop.R
import io.lb.lbgameshop.core.presentation.widgets.DefaultTextButton
import io.lb.lbgameshop.core.util.toCurrencyString
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun OrderDetailsScreen(
    navController: NavController,
    order: Order,
    items: List<OrderItem>,
    onClickItem: ((Game) -> Unit)? = null,
    onClickDelete: ((Game) -> Unit)? = null,
    onRestoreItem: (() -> Unit?)? = null,
    onClickFinish: (() -> Unit?)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
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
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                val sum = items.sumOf { Game.fromJson(it.data).salePrice }
                Row(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        bottom = 8.dp
                    ),
                ) {
                    Text(
                        text = "Total price",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = sum.toCurrencyString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            onClickFinish?.let {
                if (order.isFinished.not()) {
                    item {
                        DefaultTextButton(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            text = "Finish order"
                        ) {
                            onClickFinish.invoke()
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(
                        color = MaterialTheme.colorScheme.secondary,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            items(items) {
                val game = Game.fromJson(it.data)

                OrderItemCard(
                    game = game,
                    onClickDelete =
                    if (onClickDelete != null) {
                        {
                            onClickDelete.invoke(game)

                            coroutineScope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = context.getString(R.string.item_deleted),
                                    actionLabel = context.getString(R.string.undo)
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onRestoreItem?.invoke()
                                }
                            }
                        }
                    } else null
                ) {
                    onClickItem?.invoke(game)
                }
            }
        }
    }
}
