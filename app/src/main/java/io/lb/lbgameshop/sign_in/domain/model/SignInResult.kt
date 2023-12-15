package io.lb.lbgameshop.sign_in.domain.model

/**
 * Result of an authentication attempt.
 */
data class SignInResult(
    val data: UserData?,
    val errorMessage: String? = null
)
