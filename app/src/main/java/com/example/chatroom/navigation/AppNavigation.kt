package com.example.chatroom.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroom.screens.BrowseRoomsScreen
import com.example.chatroom.screens.ChatScreen
import com.example.chatroom.screens.CreateRoomScreen
import com.example.chatroom.screens.HomeScreen
import com.example.chatroom.screens.SettingsScreen
import com.example.chatroom.screens.RoomCreatedScreen

@Composable
fun AppNavigation(context: Context, userId: String) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("browseRooms") { BrowseRoomsScreen(navController, context) }
        composable("createRoom") { CreateRoomScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("roomCreated/{message}") { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: "No message"
            RoomCreatedScreen(navController, message)
        }
        composable("chat/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            ChatScreen(navController, roomId, context, userId) // Przekazanie userId
        }
    }
}
