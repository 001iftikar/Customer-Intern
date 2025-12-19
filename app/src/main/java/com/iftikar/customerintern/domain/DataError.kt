package com.iftikar.customerintern.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        USER_CREATION_FAILED,
        INVALID_EMAIL,
        NO_INTERNET,
        AUTH_FAILED,
        PARSING_ERROR,
        UNKNOWN,
        SERVER
    }
}