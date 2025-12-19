package com.iftikar.customerintern.presentation.destination_details_screen

import com.iftikar.customerintern.domain.model.Booking
import com.iftikar.customerintern.domain.model.Destination

data class DestinationDetailScreenState(
    val destination: Destination = Destination(),
    val booking: Booking? = null,
    val error: String? = null
)
