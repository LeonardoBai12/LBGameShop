@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)

package io.lb.lbgameshop.core.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.lb.lbgameshop.core.presentation.MainActivity

typealias LBComposeRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
