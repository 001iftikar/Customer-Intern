package com.iftikar.customerintern.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.iftikar.customerintern.domain.model.Destination
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Auth : Route {
        @Serializable
        data object LandingScreen : Route
        @Serializable
        data object SignInScreen : Route
        @Serializable
        data object OtpVerificationScreen : Route
    }

    @Serializable
    data object DestinationReserve : Route {
        @Serializable
        data object BrowsingScreen : Route
        @Serializable
        data class DestinationDetailsScreen(val destinationId: String) : Route
    }
}