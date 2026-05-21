package com.example.mobiletest.data.repository

import com.example.mobiletest.data.local.BookingDao
import com.example.mobiletest.data.local.BookingEntity
import com.example.mobiletest.data.model.Booking
import com.example.mobiletest.data.service.BookingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed class BookingResult {
    data class Success(val booking: Booking) : BookingResult()
    data class Error(val message: String) : BookingResult()
}

class BookingRepository(
    private val service: BookingService,
    private val dao: BookingDao
) {

    var counter: Int = 0;

    companion object {
        private const val SHIP_REFERENCE = "ABCDEF"
    }

    fun observeBooking(): Flow<BookingResult> =
        dao.observeBooking(SHIP_REFERENCE)
            .map { entity ->
                if (entity != null) {
                    BookingResult.Success(entity.toModel())
                } else {
                    BookingResult.Error("No cached data")
                }
            }
            .catch { e -> emit(BookingResult.Error(e.message ?: "Unknown error")) }

    suspend fun getBooking(): BookingResult {
        return try {
            val cached = dao.getBooking(SHIP_REFERENCE)
            if (cached != null && !isCacheExpired(cached.expiryTime)) {
                return BookingResult.Success(cached.toModel())
            }
            refreshBooking()
        } catch (e: Exception) {
            refreshBooking()
        }
    }

    suspend fun refreshBooking(): BookingResult {
        return try {
//            val booking = service.fetchBooking()
            val booking = service.fetchBooking().copy(
//                duration = (System.currentTimeMillis() / 1000).toInt()
                duration = counter++
            )
            dao.insertBooking(booking.toEntity())
            BookingResult.Success(booking)
        } catch (e: Exception) {
            val cached = dao.getBooking(SHIP_REFERENCE)
            if (cached != null) {
                BookingResult.Success(cached.toModel())
            } else {
                BookingResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    // expiryTime is a Unix timestamp in seconds
    private fun isCacheExpired(expiryTime: String): Boolean {
        val expiryMs = expiryTime.toLongOrNull()?.times(1000) ?: return true
        return System.currentTimeMillis() > expiryMs
    }

    private fun Booking.toEntity() = BookingEntity(
        shipReference = shipReference,
        shipToken = shipToken,
        canIssueTicketChecking = canIssueTicketChecking,
        expiryTime = expiryTime,
        duration = duration,
        segments = segments
    )

    private fun BookingEntity.toModel() = Booking(
        shipReference = shipReference,
        shipToken = shipToken,
        canIssueTicketChecking = canIssueTicketChecking,
        expiryTime = expiryTime,
        duration = duration,
        segments = segments
    )
}
