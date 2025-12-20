package com.iftikar.customerintern.mappers

import com.iftikar.customerintern.data.dto.DestinationResponseDto
import com.iftikar.customerintern.domain.model.Destination

fun DestinationResponseDto.toDestination(): Destination {
    return Destination(
        id = id,
        name = name,
        location = location,
        description = description,
        cost = cost.toString(),
        rating = rating.toString(),
        ratingCount = ratingCount.toString(),
        isActive = isActive,
        imageUrl = imageUrl
    )
}