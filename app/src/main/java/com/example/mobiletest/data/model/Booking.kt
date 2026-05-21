package com.example.mobiletest.data.model

import androidx.compose.runtime.Stable

data class Booking(
    val shipReference: String,
    val shipToken: String,
    val canIssueTicketChecking: Boolean,
    val expiryTime: String,
    val duration: Int,
    val segments: List<Segment>
)

@Stable
data class Segment(
    val id: Int,
    val originAndDestinationPair: OriginAndDestinationPair
)

data class OriginAndDestinationPair(
    val destination: Port,
    val destinationCity: String,
    val origin: Port,
    val originCity: String
)

data class Port(
    val code: String,
    val displayName: String,
    val url: String
)
