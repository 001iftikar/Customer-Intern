package com.iftikar.customerintern.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iftikar.customerintern.presentation.landing_screen.LandingScreen
import com.iftikar.customerintern.presentation.registration_screen.AuthViewModel
import com.iftikar.customerintern.presentation.registration_screen.OtpVerificationScreen
import com.iftikar.customerintern.presentation.registration_screen.SignInScreen

@Composable
fun NavigationRoot() {
    val context = LocalContext.current
    val backstack = rememberNavBackStack(Route.LandingScreen)
    NavDisplay(
        backStack = backstack,
        entryProvider = entryProvider {
            val authViewModel = hiltViewModel<AuthViewModel>()
            entry<Route.LandingScreen> {
                LandingScreen(onNavigate = {
                    backstack.add(Route.SignInScreen)
                })
            }
            entry<Route.SignInScreen> {
                SignInScreen(
                    viewModel = authViewModel,
                    onSuccess = { backstack.add(Route.OtpVerificationScreen) },
                    onNavigateBack = {
                        backstack.remove(Route.SignInScreen)
                    }
                )
            }

            entry<Route.OtpVerificationScreen> {
                OtpVerificationScreen(
                    viewModel = authViewModel,
                    onContinue = {
                        Toast
                            .makeText(context, "Verified", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onNavigateBack = {
                        backstack.remove(Route.OtpVerificationScreen)
                    }
                )
            }
        }
    )
}