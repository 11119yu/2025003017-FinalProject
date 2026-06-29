package com.example.studyroomreserve.data.network.dto

import com.example.studyroomreserve.data.entity.StudyRoom

data class StudyRoomDto(
    val roomId: Int,
    val building: String,
    val roomName: String,
    val totalSeat: Int,
    val freeSeat: Int,
    val openTime: String
) {
    fun toEntity(): StudyRoom = StudyRoom(
        roomId = roomId,
        building = building,
        roomName = roomName,
        totalSeat = totalSeat,
        freeSeat = freeSeat,
        openTime = openTime
    )
}