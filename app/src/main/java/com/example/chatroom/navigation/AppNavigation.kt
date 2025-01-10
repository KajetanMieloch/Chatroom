package com.example.chatroom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroom.screens.ChatScreen
import com.example.chatroom.screens.CreateRoomScreen
import com.example.chatroom.screens.HomeScreen
import com.example.chatroom.screens.SettingsScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("chat/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            ChatScreen(navController, roomId)
        }
        composable("createRoom") { CreateRoomScreen(navController) }
        composable("settings") { SettingsScreen(navController) } // Dodanie ekranu ustawień
    }
}
