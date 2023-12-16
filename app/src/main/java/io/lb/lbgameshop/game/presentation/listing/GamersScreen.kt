package io.lb.lbgameshop.game.presentation.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.lb.lbgameshop.R
import io.lb.lbgameshop.core.presentation.navigation.DrawerBody
import io.lb.lbgameshop.core.presentation.navigation.DrawerHeader
import io.lb.lbgameshop.core.presentation.navigation.MenuItem
import io.lb.lbgameshop.core.presentation.widgets.OrderAppBar
import io.lb.lbgameshop.core.util.DefaultSearchBar
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun GamersScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val search = remember {
        mutableStateOf("")
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader(userData = userData)
                DrawerBody(
                    items = listOf(
                        MenuItem(
                            id = "Home",
                            title = stringResource(id = R.string.home),
                            contentDescription = "Home Button",
                            icon = Icons.Default.Home
                        ),
                        MenuItem(
                            id = "Logout",
                            title = stringResource(id = R.string.logout),
                            contentDescription = "Logout Button",
                            icon = Icons.Default.ExitToApp
                        )
                    ),
                    onItemClick = {
                        when (it.id) {
                            "Home" -> {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }

                            "Logout" -> {
                                onSignOut.invoke()
                            }
                        }
                    }
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                OrderAppBar {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                    },
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Create new task",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaultSearchBar(
                    search = search,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                    onSearch = { filter ->
                    },
                )

                LazyColumn {
                }
            }
        }
    }
}
