package com.example.mobiletest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
//@TypeConverters(SegmentListConverter::class)
abstract class BookingDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: BookingDatabase? = null

        fun getInstance(context: Context): BookingDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BookingDatabase::class.java,
                    "booking_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
