package io.lb.lbgameshop.sign_in.presentation.sing_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
