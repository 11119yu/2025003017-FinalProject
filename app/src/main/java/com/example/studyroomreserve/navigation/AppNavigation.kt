package com.example.studyroomreserve.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studyroomreserve.ui.screens.BookScreen
import com.example.studyroomreserve.ui.screens.HomeScreen
import com.example.studyroomreserve.ui.screens.MyBookingScreen
import com.example.studyroomreserve.ui.screens.SettingScreen
import com.example.studyroomreserve.viewmodel.StudyViewModel

// 路由常量
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Book : Routes("book/{roomId}") {
        fun createRoute(roomId: Int) = "book/$roomId"
    }
    object MyBooking : Routes("my_booking")
    object Setting : Routes("setting")
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: StudyViewModel) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        // 首页：自习室列表
        composable(Routes.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onRoomClick = { roomId ->
                    navController.navigate(Routes.Book.createRoute(roomId))
                },
                onMyBookingClick = {
                    navController.navigate(Routes.MyBooking.route)
                },
                onSettingClick = {
                    navController.navigate(Routes.Setting.route)
                }
            )
        }

        // 预约详情页
        composable(
            route = Routes.Book.route,
            arguments = listOf(navArgument("roomId") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getInt("roomId") ?: 0
            BookScreen(
                roomId = roomId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 我的预约页
        composable(Routes.MyBooking.route) {
            MyBookingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 设置页
        composable(Routes.Setting.route) {
            SettingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}