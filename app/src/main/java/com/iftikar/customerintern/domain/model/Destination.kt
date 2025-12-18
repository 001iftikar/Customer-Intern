package com.iftikar.customerintern.domain.model

data class Destination(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val cost: Int = 0,
    val rating: Double = 0.0,
    val ratingCount: Int = 0,
)
