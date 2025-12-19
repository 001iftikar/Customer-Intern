package com.iftikar.customerintern.data.repository

import android.util.Log
import com.descope.Descope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.iftikar.customerintern.data.dto.DestinationResponseDto
import com.iftikar.customerintern.data.dto.UserRequestDto
import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult
import com.iftikar.customerintern.domain.Result
import com.iftikar.customerintern.domain.model.Booking
import com.iftikar.customerintern.domain.model.BookingStatus
import com.iftikar.customerintern.domain.model.Destination
import com.iftikar.customerintern.domain.repository.BookingRepository
import com.iftikar.customerintern.mappers.toDestination
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

class BookingRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : BookingRepository {
    override fun createUser(): Flow<EmptyResult<DataError>> = callbackFlow<EmptyResult<DataError>> {
        try {
            val descopeUser =
                Descope.sessionManager.session?.user ?: throw NullPointerException("User not found")
            // throws user back to starting screen
            val userId = descopeUser.userId
            val email = descopeUser.email ?: ""
            val name = email.substringBefore("@")
            val userRequest = UserRequestDto(
                id = userId,
                email = email,
                name = name
            )
            val userDoc = db.collection("users").document(userId)
            userDoc
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        trySend(Result.Success(Unit))
                    } else {
                        userDoc.set(userRequest)
                            .addOnSuccessListener {
                                trySend(Result.Success(Unit))
                            }
                            .addOnFailureListener { e ->
                                trySend(Result.Error(DataError.Remote.USER_CREATION_FAILED))
                            }
                    }
                }
                .addOnFailureListener {
                    trySend(Result.Error(DataError.Remote.UNKNOWN))
                }
            awaitClose { }
        } catch (ex: IOException) {
            trySend(Result.Error(DataError.Remote.NO_INTERNET))
        } catch (ex: Exception) {
            Log.e("Booking - create user", "createUser: $ex")
            trySend(Result.Error(DataError.Remote.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override fun getDestinations(): Flow<Result<List<Destination>, DataError>> =
        callbackFlow {

            val listenerRegistration = db
                .collection(FirebaseCollections.DESTINATIONS)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(Result.Error(DataError.Remote.UNKNOWN))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val destinations = snapshot
                            .toObjects<DestinationResponseDto>()
                            .map { it.toDestination() }

                        trySend(Result.Success(destinations))
                    }
                }

            awaitClose {
                listenerRegistration.remove()
            }
        }.flowOn(Dispatchers.IO)

    override fun getSpecificDestination(id: String): Flow<Result<Destination, DataError>> =
        callbackFlow<Result<Destination, DataError>> {
            val listenerRegistration = db
                .collection(FirebaseCollections.DESTINATIONS)
                .document(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.Error(DataError.Remote.UNKNOWN))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val destination = snapshot
                            .toObject<DestinationResponseDto>()
                            ?.toDestination()

                        if (destination == null) {
                            trySend(Result.Error(DataError.Remote.PARSING_ERROR))
                        }
                        trySend(Result.Success(destination!!))
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun createBooking(destinationId: String): EmptyResult<DataError> = withContext(
        Dispatchers.IO
    ) {
        try {
            // no check if session exists, check is already done when choosing route,
            // so if meanwhile it is server error, gets cached in Exception
            val user = Descope.sessionManager.session?.user
            val userId = user!!.userId
            val booking = Booking(
                userId = userId,
                destinationId = destinationId,
                status = BookingStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )
            if (hasPendingBooking(userId, destinationId)) {
                // do nothing
                Log.e("Destination", "createBooking: Already has pending booking", )
                return@withContext Result.Success(Unit)
            } else {
                db.collection(FirebaseCollections.BOOKINGS)
                    .add(booking)
                    .addOnSuccessListener {
                        Result.Success(Unit)
                    }
                    .addOnFailureListener { _ ->
                        Result.Error(DataError.Remote.PARSING_ERROR)
                    }
            }
            Result.Success(Unit)
        } catch (ex: Exception) {
            Log.e("Destination", "Error creating", ex)
            Result.Error(DataError.Remote.SERVER)
        }
    }

    override fun getBooking(destinationId: String): Flow<Result<Booking?, DataError>> = callbackFlow<Result<Booking?, DataError>> {
        val user = Descope.sessionManager.session?.user
        val userId = user!!.userId

        val listener = db
            .collection(FirebaseCollections.BOOKINGS)
            .whereEqualTo("userId", userId)
            .whereEqualTo("destinationId", destinationId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Result.Error(DataError.Remote.PARSING_ERROR))
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    trySend(Result.Success(null))
                }

                if (snapshot?.isEmpty == false) {
                    val booking = snapshot
                        .first()
                        .toObject<Booking>()

                    trySend(Result.Success(booking))
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)
    private suspend fun hasPendingBooking(
        userId: String,
        destinationId: String
    ): Boolean {
        val snapshot = db
            .collection(FirebaseCollections.BOOKINGS)
            .whereEqualTo("userId", userId)
            .whereEqualTo("destinationId", destinationId)
            .whereEqualTo("status", BookingStatus.PENDING.name)
            .limit(1)
            .get()
            .await()

        return !snapshot.isEmpty
    }


}




















