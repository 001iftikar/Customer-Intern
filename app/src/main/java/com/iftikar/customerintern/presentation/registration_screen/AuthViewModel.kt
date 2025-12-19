package com.iftikar.customerintern.presentation.registration_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.onError
import com.iftikar.customerintern.domain.onSuccess
import com.iftikar.customerintern.domain.repository.AuthRepository
import com.iftikar.customerintern.domain.repository.BookingRepository
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
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
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
            RegistrationScreensAction.OnContinueClick -> createUser()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            startLoading()
            authRepository.signIn(email = _state.value.email).onSuccess {
                stopLoading()
                _event.send(RegistrationScreensEvent.OnOtpSendSuccess)
            }.onError { error ->
                when(error) {
                    DataError.Remote.INVALID_EMAIL -> setError("Invalid email.")
                    DataError.Remote.NO_INTERNET -> setError("Please check your internet connection and try again.")
                    DataError.Remote.UNKNOWN -> setError("Oops, some unexpected error occurred!")
                    else -> setError("Oops, some unexpected error occurred!")
                }
            }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            startLoading()
            authRepository.verifyOtp(_state.value.email, _state.value.otp).onSuccess {
                _state.update {
                    it.copy(isVerificationSucceeded = true, isLoading = false)
                }
            }.onError { error ->
                when(error) {
                    DataError.Remote.AUTH_FAILED -> setError("Otp verification failed, please try again.")
                    DataError.Remote.NO_INTERNET -> setError("Please check your internet connection and try again.")
                    DataError.Remote.UNKNOWN -> setError("Oops, some unexpected error occurred!")
                    else -> setError("Oops, some unexpected error occurred!")
                }
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            startLoading()
            bookingRepository.createUser().collect { result ->
                result.onSuccess {
                    stopLoading()
                    _event.send(RegistrationScreensEvent.OnContinue)
                }.onError { error ->
                    when(error) {
                        DataError.Remote.USER_CREATION_FAILED -> setError("User creation failed")
                        DataError.Remote.NO_INTERNET -> setError("Please check your internet connection")
                        DataError.Remote.UNKNOWN -> setError("Oops, some unexpected error occurred!")
                        else -> setError("Oops, some unexpected error occurred!")
                    }
                }
            }
        }
    }


    private fun startLoading() {
        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
    }

    private fun stopLoading() {
        _state.update {
            it.copy(isLoading = false, error = null)
        }
    }

    private fun setError(error: String) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error
            )
        }
    }
}
























