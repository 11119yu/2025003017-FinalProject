package com.example.studyroomreserve

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.studyroomreserve.navigation.AppNavigation
import com.example.studyroomreserve.ui.theme.StudyRoomReserveTheme
import com.example.studyroomreserve.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: StudyViewModel = viewModel()
            val navController = rememberNavController()
            val darkMode by viewModel.darkMode.collectAsState()

            StudyRoomReserveTheme(darkTheme = darkMode) {
                AppNavigation(navController = navController, viewModel = viewModel)
            }
        }
    }
}