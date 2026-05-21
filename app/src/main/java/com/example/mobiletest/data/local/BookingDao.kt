package com.example.mobiletest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM booking WHERE shipReference = :shipReference")
    fun observeBooking(shipReference: String): Flow<BookingEntity?>

    @Query("SELECT * FROM booking WHERE shipReference = :shipReference")
    suspend fun getBooking(shipReference: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("DELETE FROM booking WHERE shipReference = :shipReference")
    suspend fun deleteBooking(shipReference: String)
}
