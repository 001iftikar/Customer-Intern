package com.iftikar.customerintern.data.repository

import android.util.Log
import com.descope.Descope
import com.descope.session.DescopeSession
import com.descope.types.DeliveryMethod
import com.descope.types.DescopeException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.iftikar.customerintern.data.dto.UserResponseDto
import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult
import com.iftikar.customerintern.domain.Result
import com.iftikar.customerintern.domain.model.User
import com.iftikar.customerintern.domain.repository.AuthRepository
import com.iftikar.customerintern.mappers.toUser
import com.iftikar.customerintern.utils.FirebaseCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : AuthRepository {
    override suspend fun signIn(email: String): EmptyResult<DataError> = withContext(Dispatchers.IO) {
        try {
            Descope.otp.signUp(method = DeliveryMethod.Email, loginId = email)

            Result.Success(Unit)
        } catch (ex: IOException) {
            Result.Error(DataError.Remote.NO_INTERNET)
        } catch (ex: DescopeException) {
            if (ex.code == "E011002") {
                Result.Error(DataError.Remote.INVALID_EMAIL)
            } else {
                Result.Error(DataError.Remote.UNKNOWN)
            }
        }
        catch (ex: Exception) {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): EmptyResult<DataError> = withContext(
        Dispatchers.IO) {
        try {
            val authResponse = Descope.otp.verify(method = DeliveryMethod.Email, loginId = email, code = code)
            val session = DescopeSession(authResponse)
            Descope.sessionManager.manageSession(session)
            Result.Success(Unit)
        } catch (ex: IOException) {
            Result.Error(DataError.Remote.NO_INTERNET)
        } catch (ex: DescopeException) {
            when(ex) {
                DescopeException.wrongOtpCode,
                DescopeException.invalidRequest -> Result.Error(DataError.Remote.AUTH_FAILED)
                else -> {
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (ex: Exception) {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override fun getUser(): Flow<Result<User, DataError>> = callbackFlow<Result<User, DataError>> {
        try {
            val user = Descope.sessionManager.session?.user
            val userId = user?.userId ?: throw NullPointerException("User not found")
            val listener = db.collection(FirebaseCollections.USERS)
                .document(userId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        Log.e("Auth", "getUser: ", error)
                        trySend(Result.Error(DataError.Remote.UNKNOWN))
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val user = snapshot.toObject<UserResponseDto>()
                        if (user != null) {
                            trySend(Result.Success(user.toUser()))
                        } else {
                            trySend(Result.Error(DataError.Remote.PARSING_ERROR))
                        }
                    } else {
                        trySend(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
            awaitClose { listener.remove() }

        } catch (ex: Exception) {
            Log.e("Auth", "getUser: ", ex)
            trySend(Result.Error(DataError.Remote.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUserName(name: String): EmptyResult<DataError> = withContext(
        Dispatchers.IO) {
        try {
            val user = Descope.sessionManager.session?.user
            val userId = user?.userId ?: throw NullPointerException("User not found")
            db.collection(FirebaseCollections.USERS)
                .document(userId)
                .update("name", name)
                .await()

            Result.Success(Unit)
        } catch (ex: Exception) {
            Log.e("Auth", "getUser: ", ex)
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}






































