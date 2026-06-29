package com.example.studyroomreserve.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studyroomreserve.data.dao.BookRecordDao
import com.example.studyroomreserve.data.dao.StudyRoomDao
import com.example.studyroomreserve.data.entity.BookRecord
import com.example.studyroomreserve.data.entity.StudyRoom

@Database(
    entities = [StudyRoom::class, BookRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyRoomDao(): StudyRoomDao
    abstract fun bookRecordDao(): BookRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_room_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}