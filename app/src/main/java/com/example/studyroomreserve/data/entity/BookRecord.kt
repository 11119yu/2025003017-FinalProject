package com.example.studyroomreserve.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_record")
data class BookRecord(
    @PrimaryKey(autoGenerate = true) val recordId: Long = 0,
    val roomId: Int,
    val building: String,
    val roomName: String,
    val bookDate: String,
    val timeSlot: String,
    val createTime: Long,
    val isCancel: Boolean = false
)