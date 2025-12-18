package com.iftikar.customerintern.presentation.registration_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.customerintern.domain.onError
import com.iftikar.customerintern.domain.onSuccess
import com.iftikar.customerintern.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationScreensState())
    val state = _state.asStateFlow()

    private val _event: Channel<RegistrationScreensEvent> = Channel()
    val event = _event.receiveAsFlow()

    fun onAction(action: RegistrationScreensAction) {
        when (action) {
            is RegistrationScreensAction.OnEmailChange -> {
                _state.update {
                    it.copy(email = action.email)
                }
            }

            is RegistrationScreensAction.OnOtpChange -> {
                _state.update {
                    it.copy(otp = action.otp)
                }
            }

            RegistrationScreensAction.OnSignInClick -> signIn()

            RegistrationScreensAction.OnVerifyOtpClick -> verifyOtp()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            authRepository.signIn(email = _state.value.email).onSuccess {
                _event.send(RegistrationScreensEvent.OnOtpSendSuccess)
            }.onError { error ->
                // TODO: SHOW ERROR
            }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            authRepository.verifyOtp(_state.value.email, _state.value.otp).onSuccess {
                _state.update {
                    it.copy(isVerificationSucceeded = true)
                }
            }.onError { error ->
                // TODO: SHOW ERROR
            }
        }
    }
}
























