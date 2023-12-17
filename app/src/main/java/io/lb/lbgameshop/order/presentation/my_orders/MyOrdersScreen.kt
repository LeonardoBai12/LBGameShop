package io.lb.lbgameshop.order.presentation.my_orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.lb.lbgameshop.core.presentation.widgets.DefaultCard
import io.lb.lbgameshop.core.util.toFormattedDate
import io.lb.lbgameshop.order.domain.model.Order

@ExperimentalMaterial3Api
@Composable
fun MyOrdersScreen(
    navController: NavController,
    state: MyOrdersState,
    onClickOrder: (Order) -> Unit
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
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(state.orders) {
                DefaultCard(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        onClickOrder.invoke(it)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Order", color = MaterialTheme.colorScheme.primary)
                            Text(text = "#${it.uuid}", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(text = "Finished date: ", color = MaterialTheme.colorScheme.primary)
                            Text(text = "${it.finishedDate?.toLong()?.toFormattedDate()}")
                        }
                    }
                }
            }
        }
    }
}
