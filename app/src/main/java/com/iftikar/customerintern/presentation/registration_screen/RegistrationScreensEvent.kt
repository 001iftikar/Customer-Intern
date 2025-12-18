package com.iftikar.customerintern.presentation.registration_screen

sealed interface RegistrationScreensEvent {
    data object Idle: RegistrationScreensEvent
    data object OnOtpSendSuccess : RegistrationScreensEvent
    data object OnContinue : RegistrationScreensEvent // TODO: CREATE USER IN FIRESTORE AND REDIRECT TO MAIN SCREEN
}