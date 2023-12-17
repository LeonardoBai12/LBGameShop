package io.lb.lbgameshop.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.lb.lbgameshop.R
import io.lb.lbgameshop.core.presentation.navigation.MainScreens
import io.lb.lbgameshop.core.util.GAME
import io.lb.lbgameshop.core.util.ORDER
import io.lb.lbgameshop.core.util.ORDER_ITEMS
import io.lb.lbgameshop.core.util.Toaster
import io.lb.lbgameshop.core.util.toOrderItemList
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.presentation.details.GameDetailsScreen
import io.lb.lbgameshop.game.presentation.listing.GameEvent
import io.lb.lbgameshop.game.presentation.listing.GamesScreen
import io.lb.lbgameshop.game.presentation.listing.GamesViewModel
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.toJson
import io.lb.lbgameshop.order.presentation.my_orders.MyOrderEvent
import io.lb.lbgameshop.order.presentation.my_orders.MyOrdersScreen
import io.lb.lbgameshop.order.presentation.my_orders.MyOrdersViewModel
import io.lb.lbgameshop.order.presentation.new_order.NewOrderEvent
import io.lb.lbgameshop.order.presentation.new_order.NewOrderScreen
import io.lb.lbgameshop.order.presentation.new_order.NewOrderViewModel
import io.lb.lbgameshop.order.presentation.widgets.OrderDetailsScreen
import io.lb.lbgameshop.sign_in.presentation.SignInScreen
import io.lb.lbgameshop.sign_in.presentation.sing_in.SignInEvent
import io.lb.lbgameshop.sign_in.presentation.sing_in.SignInViewModel
import io.lb.lbgameshop.ui.theme.LBGameShopTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Activity on which every screen of the app is displayed.
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var toaster: Toaster

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LBGameShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val signInViewModel = hiltViewModel<SignInViewModel>()
                    val signInState = signInViewModel.state.collectAsState().value

                    val gameViewModel = hiltViewModel<GamesViewModel>()
                    val gameState = gameViewModel.state.collectAsState().value

                    val newOrderViewModel = hiltViewModel<NewOrderViewModel>()
                    val newOrderState = newOrderViewModel.state.collectAsState().value

                    val myOrdersViewModel = hiltViewModel<MyOrdersViewModel>()
                    val myOrdersState = myOrdersViewModel.state.collectAsState().value

                    var startDestination = MainScreens.SignInScreen.name

                    signInViewModel.currentUser?.let {
                        startDestination = MainScreens.GamesScreen.name
                        newOrderViewModel.userData = it
                        myOrdersViewModel.userData = it
                    }

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(MainScreens.SignInScreen.name) {
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        signInViewModel.onEvent(
                                            SignInEvent.RequestSignInWithGoogle(result.data)
                                        )
                                    }
                                }
                            )

                            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                if (signInState.isSignInSuccessful) {
                                    toaster.showToast(R.string.sign_in_successful)

                                    navController.navigate(MainScreens.GamesScreen.name)
                                    signInViewModel.resetState()
                                }
                            }

                            LaunchedEffect(key1 = "launchedEffectKey") {
                                signInViewModel.eventFlow.collectLatest { event ->
                                    when (event) {
                                        is SignInViewModel.UiEvent.ShowToast -> {
                                            toaster.showToast(event.message)
                                        }
                                    }
                                }
                            }

                            SignInScreen(
                                onSignInWithGoogleClick = {
                                    lifecycleScope.launch {
                                        signInViewModel.signIn()?.let {
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    it
                                                ).build()
                                            )
                                        } ?: toaster.showToast(
                                            R.string.something_went_wrong
                                        )
                                    }
                                },
                                onSignInClick = { email, password, repeatedPassword ->
                                    signInViewModel.onEvent(
                                        SignInEvent.RequestSignIn(
                                            email = email,
                                            password = password,
                                            repeatedPassword = repeatedPassword
                                        )
                                    )
                                },
                                onLoginClick = { email, password ->
                                    signInViewModel.onEvent(
                                        SignInEvent.RequestLogin(
                                            email = email,
                                            password = password,
                                        )
                                    )
                                }
                            )
                        }

                        composable(MainScreens.GamesScreen.name) {
                            signInViewModel.onEvent(SignInEvent.LoadSignedInUser)
                            val userData = signInViewModel.currentUser

                            LaunchedEffect(key1 = "repeatOnLifecycleGetOrder") {
                                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    newOrderViewModel.getOrder()
                                }
                            }

                            LaunchedEffect(key1 = MainScreens.GamesScreen.name) {
                                gameViewModel.eventFlow.collectLatest { event ->
                                    when (event) {
                                        is GamesViewModel.UiEvent.ShowToast -> {
                                            toaster.showToast(event.message)
                                        }
                                    }
                                }
                            }

                            GamesScreen(
                                navController = navController,
                                userData = userData,
                                state = gameState,
                                onSignOut = {
                                    lifecycleScope.launch {
                                        signInViewModel.onEvent(SignInEvent.RequestLogout)
                                        toaster.showToast(R.string.signed_out)
                                        navController.navigate(MainScreens.SignInScreen.name) {
                                            popUpTo(MainScreens.GamesScreen.name) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                },
                                onClickMyOrders = {
                                    navController.navigate(MainScreens.MyOrdersScreen.name)
                                },
                                onClickTryAgain = {
                                    gameViewModel.getGames()
                                },
                                onSearchGame = { filter ->
                                    gameViewModel.onEvent(GameEvent.SearchedForGame(filter))
                                },
                                onClickFab = {
                                    navController.navigate(MainScreens.NewOrderScreen.name)
                                }
                            )
                        }

                        composable(
                            route = MainScreens.GameDetailsScreen.name + "/{$GAME}",
                            arguments = listOf(
                                navArgument(name = GAME) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getString(GAME)?.let { json ->
                                val gameFromJson = Game.fromJson(json)
                                val isFinishedOrder = newOrderState.order.isFinished
                                val isInTheCart = newOrderState.items.any {
                                    Game.fromJson(it.data).gameID == gameFromJson.gameID
                                }

                                GameDetailsScreen(
                                    navController = navController,
                                    game = gameFromJson,
                                    isInTheCart = isInTheCart,
                                    isFinishedOrder = isFinishedOrder,
                                    onClickAddToCart = { game ->
                                        if (isInTheCart.not()) {
                                            newOrderViewModel.onEvent(
                                                NewOrderEvent.RequestInsert(game)
                                            )
                                        } else {
                                            newOrderViewModel.onEvent(
                                                NewOrderEvent.RequestDelete(
                                                    newOrderState.items.find {
                                                        Game.fromJson(it.data).gameID == gameFromJson.gameID
                                                    } ?: return@GameDetailsScreen
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        composable(
                            route = MainScreens.NewOrderScreen.name
                        ) {
                            LaunchedEffect(key1 = "repeatOnLifecycleGetOrder") {
                                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    newOrderViewModel.getOrder()
                                }
                            }

                            LaunchedEffect(key1 = "launchedEffectKey") {
                                newOrderViewModel.eventFlow.collectLatest { event ->
                                    when (event) {
                                        NewOrderViewModel.UiEvent.Finish -> {
                                            navController.navigateUp()
                                        }
                                        is NewOrderViewModel.UiEvent.ShowToast -> {
                                            toaster.showToast(event.message)
                                        }
                                    }
                                }
                            }

                            val userData = signInViewModel.currentUser
                            newOrderViewModel.userData = userData

                            NewOrderScreen(
                                navController = navController,
                                state = newOrderState,
                                onClickItem = { game ->
                                    navController.navigate(
                                        MainScreens.GameDetailsScreen.name + "/${game.toJsonEncode()}"
                                    )
                                },
                                onClickDelete = { game ->
                                    newOrderViewModel.onEvent(
                                        NewOrderEvent.RequestDelete(
                                            newOrderState.items.find {
                                                Game.fromJson(it.data).gameID == game.gameID
                                            } ?: return@NewOrderScreen
                                        )
                                    )
                                },
                                onRestoreItem = {
                                    newOrderViewModel.onEvent(NewOrderEvent.RestoreTask)
                                },
                                onClickFinish = {
                                    newOrderViewModel.onEvent(NewOrderEvent.FinishOrder)
                                }
                            )
                        }

                        composable(route = MainScreens.MyOrdersScreen.name) {
                            LaunchedEffect(key1 = "repeatOnLifecycleGetOrder") {
                                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    myOrdersViewModel.getOrders()
                                }
                            }

                            LaunchedEffect(key1 = "launchedEffectKey") {
                                myOrdersViewModel.eventFlow.collectLatest { event ->
                                    when (event) {
                                        is MyOrdersViewModel.UiEvent.NavigateToOrderDetails -> {
                                            navController.navigate(
                                                MainScreens.OrderDetailsScreen.name +
                                                    "/${event.order.toJson()}" +
                                                    "/${event.items.toJson()}"
                                            )
                                        }
                                    }
                                }
                            }

                            MyOrdersScreen(
                                navController = navController,
                                state = myOrdersState,
                                onClickOrder = {
                                    myOrdersViewModel.onEvent(
                                        MyOrderEvent.NavigateToOrderDetails(it)
                                    )
                                }
                            )
                        }

                        composable(
                            route = MainScreens.OrderDetailsScreen.name + "/{$ORDER}/{$ORDER_ITEMS}",
                            arguments = listOf(
                                navArgument(name = ORDER) {
                                    type = NavType.StringType
                                },
                                navArgument(name = ORDER_ITEMS) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val order = Order.fromJson(
                                backStackEntry.arguments?.getString(ORDER) ?: ""
                            )
                            val items = backStackEntry.arguments
                                ?.getString(ORDER_ITEMS)
                                ?.toOrderItemList() ?: emptyList()

                            OrderDetailsScreen(
                                navController = navController,
                                order = order,
                                items = items
                            )
                        }
                    }
                }
            }
        }
    }
}
