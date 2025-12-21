package com.iftikar.customerintern.mappers

import com.iftikar.customerintern.data.dto.UserResponseDto
import com.iftikar.customerintern.domain.model.User

fun UserResponseDto.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email
    )
}