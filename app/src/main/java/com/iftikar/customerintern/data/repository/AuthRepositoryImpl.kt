package com.iftikar.customerintern.data.repository

import com.descope.Descope
import com.descope.session.DescopeSession
import com.descope.types.DeliveryMethod
import com.descope.types.DescopeException
import com.iftikar.customerintern.domain.DataError
import com.iftikar.customerintern.domain.EmptyResult
import com.iftikar.customerintern.domain.Result
import com.iftikar.customerintern.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
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
}






































