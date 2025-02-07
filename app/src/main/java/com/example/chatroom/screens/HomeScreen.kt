package com.example.chatroom.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    var roomId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("browseRooms") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Browse Rooms")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = { Text("Room ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("chat/${roomId}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Join Room")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("createRoom") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Room")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("settings") }, // Przejście do ekranu ustawień
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ustawienia")
        }
    }
}
