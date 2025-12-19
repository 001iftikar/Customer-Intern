package com.iftikar.customerintern.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iftikar.customerintern.presentation.browsing_screen.BrowsingNavigation
import com.iftikar.customerintern.presentation.registration_screen.RegistrationNavigation

@Composable
fun NavigationRoot(
    startDestination: Route
) {
    val rootBackstack = rememberNavBackStack(startDestination)
    NavDisplay(
        backStack = rootBackstack,
        entryProvider = entryProvider {
            entry<Route.Auth> {
                RegistrationNavigation(
                    onContinue = {
                        rootBackstack.remove(it)
                        rootBackstack.add(Route.DestinationReserve)
                    }
                )
            }
            entry<Route.DestinationReserve> {
                BrowsingNavigation()
            }
        }
    )
}