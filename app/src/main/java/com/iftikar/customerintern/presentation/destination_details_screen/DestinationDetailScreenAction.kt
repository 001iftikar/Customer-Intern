package com.iftikar.customerintern.presentation.destination_details_screen

sealed interface DestinationDetailScreenAction {
    data class BookDestination(val id: String) : DestinationDetailScreenAction
}