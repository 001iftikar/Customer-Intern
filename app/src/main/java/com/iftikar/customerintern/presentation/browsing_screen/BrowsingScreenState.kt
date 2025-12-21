package com.iftikar.customerintern.presentation.browsing_screen

import com.iftikar.customerintern.domain.model.Destination

data class BrowsingScreenState(
    val isLoading: Boolean = false,
    val destinations: List<Destination> = emptyList(),
    val userName: String = "",
    val error: String? = null
)
