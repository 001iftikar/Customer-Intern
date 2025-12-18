package com.iftikar.customerintern.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT,
        USER_EXISTS,
        INVALID_EMAIL,
        PASSWORD_MISMATCH,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        AUTH_FAILED,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}