package com.iftikar.customerintern.presentation.destination_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.onError
import com.iftikar.customerintern.domain.onSuccess
import com.iftikar.customerintern.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationDetailScreenViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DestinationDetailScreenState())
    val state = _state.asStateFlow()

    fun getDestination(id: String) {
        viewModelScope.launch {
            bookingRepository.getSpecificDestination(id = id).collect { result ->
                result.onSuccess { destination ->
                    _state.update {
                        it.copy(destination = destination, error = null)
                    }
                }.onError { _ ->
                    _state.update {
                        it.copy(error = "Fetching error, Please try again")
                    }
                }
            }
        }
    }

    fun onAction(action: DestinationDetailScreenAction) {
        when (action) {
            is DestinationDetailScreenAction.BookDestination -> bookDestination(action.id)
        }
    }

    private fun bookDestination(id: String) {
        viewModelScope.launch {
            bookingRepository.createBooking(id).onSuccess {
                // TODO CHANGE BUTTON TEXT
            }.onError { error ->
                when (error) {
                    DataError.Remote.PARSING_ERROR -> _state.update {
                        it.copy(error = "Booking error, Please try again")
                    }

                    DataError.Remote.SERVER -> {
                        _state.update {
                            it.copy(error = "Oops, some unexpected error occurred!")
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun getBooking(destinationId: String) {
        viewModelScope.launch {
            bookingRepository.getBooking(destinationId).collect { result ->
                result.onSuccess { booking ->
                    _state.update {
                        it.copy(booking = booking)
                    }
                }
                    .onError { error ->
                        when (error) {
                            DataError.Remote.PARSING_ERROR -> {
                                _state.update {
                                    it.copy(error = "Booking fetching went wrong")
                                }
                            }

                            else -> Unit
                        }
                    }
            }
        }
    }

}






























