package com.iftikar.customerintern.domain.repository

import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult
import com.iftikar.customerintern.domain.Result
import com.iftikar.customerintern.domain.model.Booking
import com.iftikar.customerintern.domain.model.Destination
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun createUser(): Flow<EmptyResult<DataError>>
    fun getDestinations(): Flow<Result<List<Destination>, DataError>>
    fun getSpecificDestination(id: String): Flow<Result<Destination, DataError>>
    suspend fun createBooking(destinationId: String): EmptyResult<DataError>
    fun getBooking(destinationId: String): Flow<Result<Booking?, DataError>>
}