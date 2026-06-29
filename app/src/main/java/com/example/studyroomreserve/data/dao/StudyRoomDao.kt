package com.example.studyroomreserve.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.studyroomreserve.data.entity.StudyRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyRoomDao {
    @Query("SELECT * FROM study_room")
    fun getAllRooms(): Flow<List<StudyRoom>>

    @Query("SELECT * FROM study_room WHERE building LIKE '%' || :keyword || '%' OR roomName LIKE '%' || :keyword || '%'")
    fun searchRooms(keyword: String): Flow<List<StudyRoom>>

    @Query("DELETE FROM study_room")
    suspend fun clearAll()

    @Insert
    suspend fun insertAll(rooms: List<StudyRoom>)

    @Query("SELECT COUNT(*) FROM study_room")
    suspend fun countRooms(): Int
}