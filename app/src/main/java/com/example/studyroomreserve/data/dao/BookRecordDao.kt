package com.example.studyroomreserve.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studyroomreserve.data.entity.BookRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BookRecordDao {
    @Query("SELECT * FROM book_record ORDER BY bookDate DESC")
    fun getAllRecords(): Flow<List<BookRecord>>

    @Insert
    suspend fun addRecord(record: BookRecord)

    @Update
    suspend fun updateRecord(record: BookRecord)

    @Delete
    suspend fun deleteRecord(record: BookRecord)

    @Query("SELECT COUNT(*) FROM book_record WHERE roomId = :roomId AND bookDate = :date AND timeSlot = :slot AND isCancel = 0")
    suspend fun countSameBooking(roomId: Int, date: String, slot: String): Int
}