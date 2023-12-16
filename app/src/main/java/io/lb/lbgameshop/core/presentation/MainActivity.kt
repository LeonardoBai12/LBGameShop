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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.lb.lbgameshop.R
import io.lb.lbgameshop.core.presentation.navigation.MainScreens
import io.lb.lbgameshop.core.util.ORDER
import io.lb.lbgameshop.core.util.Toaster
import io.lb.lbgameshop.game.presentation.details.OrderDetailsScreen
import io.lb.lbgameshop.game.presentation.listing.GamesScreen
import io.lb.lbgameshop.game.presentation.listing.GamesViewModel
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

                    var startDestination = MainScreens.SignInScreen.name

                    signInViewModel.currentUser?.let {
                        startDestination = MainScreens.GamesScreen.name
                    }
                    gameViewModel.getGames()

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
                            GamesScreen(
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
                                onClickTryAgain = {
                                    gameViewModel.getGames()
                                }
                            )
                        }

                        composable(
                            route = MainScreens.GameDetailsScreen.name + "/{$ORDER}",
                            arguments = listOf(
                                navArgument(name = ORDER) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getString(ORDER)?.let {
                                OrderDetailsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
