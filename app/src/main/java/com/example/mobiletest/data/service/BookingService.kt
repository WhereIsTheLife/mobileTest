package com.example.mobiletest.data.service

import android.content.Context
import com.example.mobiletest.data.model.Booking
import com.google.gson.Gson

class BookingService(private val context: Context) {

    private val gson = Gson()

    suspend fun fetchBooking(): Booking {
        val json = context.assets.open("booking.json").bufferedReader().use { it.readText() }
        return gson.fromJson(json, Booking::class.java)
    }
}
