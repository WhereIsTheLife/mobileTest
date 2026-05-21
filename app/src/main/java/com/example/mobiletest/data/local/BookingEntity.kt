package com.example.mobiletest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mobiletest.data.model.Segment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "booking")
@TypeConverters(SegmentListConverter::class)
data class BookingEntity(
    @PrimaryKey val shipReference: String,
    val shipToken: String,
    val canIssueTicketChecking: Boolean,
    val expiryTime: String,
    val duration: Int,
    val segments: List<Segment>
)

class SegmentListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSegmentList(segments: List<Segment>): String =
        gson.toJson(segments)

    @TypeConverter
    fun toSegmentList(json: String): List<Segment> {
        val type = object : TypeToken<List<Segment>>() {}.type
        return gson.fromJson(json, type)
    }
}
