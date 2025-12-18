package com.iftikar.customerintern.presentation.registration_screen

sealed interface RegistrationScreensAction {
    data class OnEmailChange(val email: String) : RegistrationScreensAction
    data class OnOtpChange(val otp: String) : RegistrationScreensAction
    data object OnSignInClick : RegistrationScreensAction
    data object OnVerifyOtpClick : RegistrationScreensAction
}