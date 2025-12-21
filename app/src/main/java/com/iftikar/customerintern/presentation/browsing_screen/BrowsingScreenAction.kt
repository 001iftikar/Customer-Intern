package com.iftikar.customerintern.presentation.browsing_screen

sealed interface BrowsingScreenAction {
    data class OnUpdateName(val name: String) : BrowsingScreenAction
}