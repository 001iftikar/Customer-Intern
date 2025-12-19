package com.iftikar.customerintern.presentation.registration_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftikar.customerintern.presentation.components.ButtonComponent
import com.iftikar.customerintern.presentation.components.TopAppBarComponent
import com.iftikar.customerintern.presentation.registration_screen.components.RegistrationTextFieldComponents

@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(RegistrationScreensEvent.Idle)
    val onAction = viewModel::onAction

    LaunchedEffect(event) {
        when(event) {
            RegistrationScreensEvent.OnOtpSendSuccess -> onSuccess()
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarComponent(
            onNavigateBack = onNavigateBack
        ) }
    ) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(innerPadding)
                .padding(vertical = 90.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Sign in now",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 26.sp
            )
            Text(
                text = "Please sign in to continue our app",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(36.dp))
            RegistrationTextFieldComponents(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = {onAction(RegistrationScreensAction.OnEmailChange(it))},
                placeholder = {Text("email")}
            )
            Spacer(Modifier.height(24.dp))
            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                onClick = {onAction(RegistrationScreensAction.OnSignInClick)}
            ) {
                if (!state.isLoading) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    }
}























