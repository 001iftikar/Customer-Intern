package com.iftikar.customerintern.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object LandingScreen : Route
    @Serializable
    data object SignInScreen : Route
    @Serializable
    data object OtpVerificationScreen : Route
}