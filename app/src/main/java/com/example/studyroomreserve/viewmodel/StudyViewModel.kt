package com.example.studyroomreserve.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyroomreserve.data.entity.BookRecord
import com.example.studyroomreserve.data.entity.StudyRoom
import com.example.studyroomreserve.data.repository.StudyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StudyRepository(application)

    // 自习室列表数据流
    val roomList: Flow<List<StudyRoom>> = repository.allRooms
    // 预约记录数据流
    val recordList: Flow<List<BookRecord>> = repository.allRecords

    // 深色模式状态
    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    // 预约结果提示
    private val _toastMsg = MutableStateFlow("")
    val toastMsg: StateFlow<String> = _toastMsg.asStateFlow()

    init {
        // 初始化时读取偏好设置
        viewModelScope.launch {
            repository.darkModeFlow.collect {
                _darkMode.value = it
            }
        }
        // 首次启动初始化模拟数据
        viewModelScope.launch {
            repository.initMockDataIfNeeded()
        }
    }

    // 刷新自习室数据（模拟网络请求）
    fun refreshRooms() {
        viewModelScope.launch {
            repository.refreshRooms()
        }
    }

    // 搜索自习室
    fun searchRooms(keyword: String): Flow<List<StudyRoom>> {
        return repository.searchRooms(keyword)
    }

    // 提交预约
    fun addBooking(room: StudyRoom, date: String, timeSlot: String) {
        viewModelScope.launch {
            val record = BookRecord(
                roomId = room.roomId,
                building = room.building,
                roomName = room.roomName,
                bookDate = date,
                timeSlot = timeSlot,
                createTime = System.currentTimeMillis()
            )
            val success = repository.addBooking(record)
            _toastMsg.value = if (success) "预约成功" else "该时段已被预约，请更换时段"
        }
    }

    // 取消预约
    fun cancelBooking(record: BookRecord) {
        viewModelScope.launch {
            repository.cancelBooking(record)
            _toastMsg.value = "已取消预约"
        }
    }

    // 删除预约
    fun deleteBooking(record: BookRecord) {
        viewModelScope.launch {
            repository.deleteBooking(record)
        }
    }

    // 切换深色模式
    fun toggleDarkMode() {
        viewModelScope.launch {
            val newValue = !_darkMode.value
            repository.setDarkMode(newValue)
            _darkMode.value = newValue
        }
    }

    // 清空提示
    fun clearToast() {
        _toastMsg.value = ""
    }
}