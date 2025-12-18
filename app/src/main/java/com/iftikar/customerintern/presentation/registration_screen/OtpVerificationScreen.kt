package com.iftikar.customerintern.presentation.registration_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftikar.customerintern.presentation.components.ButtonComponent
import com.iftikar.customerintern.presentation.components.TopAppBarComponent
import com.iftikar.customerintern.ui.theme.CustomLightGray
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(
    viewModel: AuthViewModel,
    onContinue: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var text by remember(state.isVerificationSucceeded) { mutableStateOf(
        value = if (state.isVerificationSucceeded) {
            "Verification successful"
        } else {
            "Please check your email ${state.email} to see the verification code"
        }
    ) }
    var timeLeft by remember { mutableIntStateOf(90) }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    val timeText = String.format("%d:%02d", minutes, seconds)

    val onAction = viewModel::onAction

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarComponent(
            onNavigateBack = onNavigateBack
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(vertical = 90.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "OTP Verification",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 26.sp
            )
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(36.dp))
            if (!state.isVerificationSucceeded) {
                OtpInput(
                    modifier = Modifier.fillMaxWidth(),
                    onOtpComplete = { onAction(RegistrationScreensAction.OnOtpChange(it)) }
                )
                Spacer(Modifier.height(24.dp))
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Verify",
                    onClick = { onAction(RegistrationScreensAction.OnVerifyOtpClick) }
                )
                OtpResend(
                    modifier = Modifier.fillMaxWidth(),
                    timeLeft = timeText
                )
            } else {
                VerificationSuccessful(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onContinue
                )
            }
        }
    }
}

@Composable
private fun OtpInput(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit
) {
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = List(otpLength) { FocusRequester() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        otpValues.forEachIndexed { index, value ->

            TextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {

                        otpValues[index] = newValue

                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }

                        if (otpValues.all { it.isNotEmpty() }) {
                            onOtpComplete(otpValues.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(56.dp)
                    .focusRequester(focusRequesters[index])
                    .onKeyEvent {
                        if (it.key == Key.Backspace && otpValues[index].isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                            true
                        } else false
                    },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = CustomLightGray,
                    unfocusedContainerColor = CustomLightGray
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun OtpResend(
    modifier: Modifier = Modifier,
    timeLeft: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Resend code to",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = timeLeft,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun VerificationSuccessful(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ButtonComponent(
        modifier = modifier,
        text = "Continue",
        onClick = onClick
    )
}
































