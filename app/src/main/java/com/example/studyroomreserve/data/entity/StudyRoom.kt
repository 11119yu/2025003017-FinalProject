package com.example.studyroomreserve.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_room")
data class StudyRoom(
    @PrimaryKey val roomId: Int,
    val building: String,
    val roomName: String,
    val totalSeat: Int,
    val freeSeat: Int,
    val openTime: String
)