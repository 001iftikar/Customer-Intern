package com.iftikar.customerintern.presentation.registration_screen

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iftikar.customerintern.presentation.landing_screen.LandingScreen
import com.iftikar.customerintern.presentation.navigation.Route

@Composable
fun RegistrationNavigation(
    onContinue: () -> Unit
) {
    val registrationBackStack = rememberNavBackStack(Route.Auth.LandingScreen)
    val authViewModel = hiltViewModel<AuthViewModel>()
    NavDisplay(
        backStack = registrationBackStack,
        entryProvider = entryProvider {
            entry<Route.Auth.LandingScreen> {
                LandingScreen(onNavigate = {
                    registrationBackStack.add(Route.Auth.SignInScreen)
                })
            }
            entry<Route.Auth.SignInScreen> {
                SignInScreen(
                    viewModel = authViewModel,
                    onSuccess = { registrationBackStack.add(Route.Auth.OtpVerificationScreen) },
                    onNavigateBack = {
                        registrationBackStack.remove(Route.Auth.SignInScreen)
                    }
                )
            }

            entry<Route.Auth.OtpVerificationScreen> {
                OtpVerificationScreen(
                    viewModel = authViewModel,
                    onContinue = onContinue,
                    onNavigateBack = {
                        registrationBackStack.remove(Route.Auth.OtpVerificationScreen)
                    }
                )
            }
        }
    )
}