package com.iftikar.customerintern.presentation.browsing_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.customerintern.domain.onError
import com.iftikar.customerintern.domain.onSuccess
import com.iftikar.customerintern.domain.repository.AuthRepository
import com.iftikar.customerintern.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowsingScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BrowsingScreenState())
    val state = _state.asStateFlow()

    init {
        getDestinations()
    }

    fun onAction(action: BrowsingScreenAction) {
        when (action) {
            is BrowsingScreenAction.OnUpdateName -> updateUserName(action.name)
        }
    }

    private fun getDestinations() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }

            bookingRepository.getDestinations().collect { result ->
                result.onSuccess { destinations ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            destinations = destinations
                        )
                    }
                }.onError { _ ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Destinations could not be loaded! Please try again"
                        )
                    }
                }
            }
        }
    }

    // call this on launch effect
    fun getUserName() {
        viewModelScope.launch {
            authRepository.getUser().collect { result ->
                result.onSuccess { user ->
                    _state.update {
                        it.copy(userName = user.name)
                    }
                }
            }
        }
    }

    private fun updateUserName(name: String) {
        viewModelScope.launch {
            authRepository.updateUserName(name).onSuccess {
                // Do nothing
            }.onError {
                // FIGMA SAID THAT UI SHOULD MATCH EXACTLY
                // BUT IT ALSO DID NOT SPECIFY WHAT DO FOR USER NAME
                // SO I DO NOT KNOW WHERE TO SHOW ERROR, HENCE I AM JUST LOGGING THE ERROR
                // FOR THE DEVELOPERS, NOT SHOWING TO USER EXCEPT SOMETIMES IN SOME PLACES GENERIC ERROR
            }
        }
    }
}


























