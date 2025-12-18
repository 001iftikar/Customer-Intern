package com.iftikar.customerintern.presentation.registration_screen

data class RegistrationScreensState(
    val email: String = "",
    val otp: String = "",
    val isVerificationSucceeded: Boolean = false,
)
