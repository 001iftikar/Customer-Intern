package com.iftikar.customerintern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.descope.Descope
import com.iftikar.customerintern.presentation.navigation.NavigationRoot
import com.iftikar.customerintern.presentation.navigation.Route
import com.iftikar.customerintern.ui.theme.CustomerInternTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var startDestination: Route
    val session = Descope.sessionManager.session

    override fun onCreate(savedInstanceState: Bundle?) {
        if (session != null && !session.refreshToken.isExpired) {
            startDestination = Route.DestinationReserve
        } else {
            startDestination = Route.Auth
        }
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomerInternTheme {
                NavigationRoot(startDestination)
            }
        }
    }
}

























