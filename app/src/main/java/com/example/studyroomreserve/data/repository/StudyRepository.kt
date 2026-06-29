package com.example.studyroomreserve.data.repository

import android.content.Context
import com.example.studyroomreserve.data.dao.BookRecordDao
import com.example.studyroomreserve.data.dao.StudyRoomDao
import com.example.studyroomreserve.data.database.AppDatabase
import com.example.studyroomreserve.data.entity.BookRecord
import com.example.studyroomreserve.data.entity.StudyRoom
import com.example.studyroomreserve.data.network.RetrofitClient
import com.example.studyroomreserve.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class StudyRepository(context: Context) {
    private val studyRoomDao: StudyRoomDao
    private val bookRecordDao: BookRecordDao
    private val userPrefs: UserPreferencesRepository

    init {
        val db = AppDatabase.getInstance(context)
        studyRoomDao = db.studyRoomDao()
        bookRecordDao = db.bookRecordDao()
        userPrefs = UserPreferencesRepository(context)
    }

    // ========== 自习室数据 ==========
    // 响应式查询本地缓存，数据变化自动通知UI
    val allRooms: Flow<List<StudyRoom>> = studyRoomDao.getAllRooms()

    // 模糊搜索自习室
    fun searchRooms(keyword: String): Flow<List<StudyRoom>> {
        return studyRoomDao.searchRooms(keyword)
    }

    // 首次启动初始化模拟数据
    suspend fun initMockDataIfNeeded() {
        val count = studyRoomDao.countRooms()
        if (count == 0) {
            val mockRooms = listOf(
                StudyRoom(1, "图书馆", "一楼自习室A", 50, 32, "08:00-22:00"),
                StudyRoom(2, "图书馆", "二楼自习室B", 40, 15, "08:00-22:00"),
                StudyRoom(3, "教学楼A", "101教室", 60, 45, "07:30-21:30"),
                StudyRoom(4, "教学楼A", "203教室", 35, 0, "07:30-21:30"),
                StudyRoom(5, "教学楼B", "305教室", 45, 28, "08:00-22:00"),
                StudyRoom(6, "综合楼", "研讨室1", 20, 12, "09:00-20:00")
            )
            studyRoomDao.insertAll(mockRooms)
        }
    }

    // 从网络拉取最新数据并刷新本地缓存
    suspend fun refreshRooms() {
        try {
            val remoteList = RetrofitClient.api.getStudyRooms()
            studyRoomDao.clearAll()
            studyRoomDao.insertAll(remoteList.map { it.toEntity() })
        } catch (e: Exception) {
            // 网络异常时保留本地缓存，不崩溃
            e.printStackTrace()
        }
    }

    // ========== 预约记录 ==========
    val allRecords: Flow<List<BookRecord>> = bookRecordDao.getAllRecords()


    suspend fun addBooking(record: BookRecord): Boolean {
        val count = bookRecordDao.countSameBooking(
            roomId = record.roomId,
            date = record.bookDate,
            slot = record.timeSlot
        )
        return if (count > 0) {
            false // 同时段已预约，返回失败
        } else {
            bookRecordDao.addRecord(record)
            true
        }
    }

    // 取消预约
    suspend fun cancelBooking(record: BookRecord) {
        bookRecordDao.updateRecord(record.copy(isCancel = true))
    }

    // 删除预约记录
    suspend fun deleteBooking(record: BookRecord) {
        bookRecordDao.deleteRecord(record)
    }

    // ========== 偏好设置 ==========
    val darkModeFlow: Flow<Boolean> = userPrefs.darkModeFlow
    val defaultBuildingFlow: Flow<String> = userPrefs.defaultBuildingFlow

    suspend fun setDarkMode(enabled: Boolean) {
        userPrefs.setDarkMode(enabled)
    }

    suspend fun setDefaultBuilding(building: String) {
        userPrefs.setDefaultBuilding(building)
    }
}