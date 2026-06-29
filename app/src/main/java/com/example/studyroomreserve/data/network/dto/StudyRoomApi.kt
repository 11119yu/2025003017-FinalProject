package com.example.studyroomreserve.data.network

import com.example.studyroomreserve.data.network.dto.StudyRoomDto
import retrofit2.http.GET

interface StudyRoomApi {
    @GET("study_rooms.json")
    suspend fun getStudyRooms(): List<StudyRoomDto>
}