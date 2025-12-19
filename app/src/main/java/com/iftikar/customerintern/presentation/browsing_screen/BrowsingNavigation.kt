package com.iftikar.customerintern.presentation.browsing_screen

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iftikar.customerintern.presentation.destination_details_screen.DestinationDetailsScreen
import com.iftikar.customerintern.presentation.navigation.Route

@Composable
fun BrowsingNavigation() {
    val browsingBackStack = rememberNavBackStack(Route.DestinationReserve.BrowsingScreen)

    NavDisplay(
        backStack = browsingBackStack,
        entryProvider = entryProvider {
            entry<Route.DestinationReserve.BrowsingScreen> {
                BrowsingScreen(
                    onClick = { id ->
                        browsingBackStack.add(Route.DestinationReserve.DestinationDetailsScreen(id))
                    }
                )
            }
            entry<Route.DestinationReserve.DestinationDetailsScreen> { route ->

                DestinationDetailsScreen(
                    destinationId = route.destinationId,
                    onNavigateBack = { browsingBackStack.remove(route) }
                )
            }
        }
    )
}