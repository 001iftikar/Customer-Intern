package com.iftikar.customerintern.domain.repository

import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult

interface AuthRepository {
    suspend fun signIn(email: String): EmptyResult<DataError>
    suspend fun verifyOtp(email: String, code: String): EmptyResult<DataError>
}