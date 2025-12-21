package com.iftikar.customerintern.domain.repository

import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult
import com.iftikar.customerintern.domain.Result
import com.iftikar.customerintern.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String): EmptyResult<DataError>
    suspend fun verifyOtp(email: String, code: String): EmptyResult<DataError>
    fun getUser(): Flow<Result<User, DataError>>
    suspend fun updateUserName(name: String): EmptyResult<DataError>
}