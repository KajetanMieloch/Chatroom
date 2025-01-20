package com.example.chatroom.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.chatroom.utils.FileUtils
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val id: String,
    val name: String
)

@Composable
fun BrowseRoomsScreen(navController: NavController, context: Context) {
    val userId = remember { FileUtils.loadOrGenerateSecretPhrase(context) }
    var rooms by remember { mutableStateOf<List<RoomResponse>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    fun fetchRooms() {
        coroutineScope.launch {
            try {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                val response: List<RoomResponse> = client.get("http://10.0.2.2:9090/getRooms") {
                    parameter("userId", userId)
                }.body()
                rooms = response
                client.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun leaveRoom(roomId: String) {
        coroutineScope.launch {
            try {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                client.post("http://10.0.2.2:9090/leaveRoom") {
                    parameter("userId", userId)
                    parameter("roomId", roomId)
                }
                fetchRooms()
                client.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            fetchRooms()
            delay(3000L)
        }
    }

    // Layout ekranu
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Browse Rooms",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    onJoinClick = { navController.navigate("chat/${room.id}") },
                    onLeaveClick = { leaveRoom(room.id) }
                )
            }
        }
    }
}

@Composable
fun RoomItem(
    room: RoomResponse,
    onJoinClick: () -> Unit,
    onLeaveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onJoinClick() }
            )
            Button(onClick = onLeaveClick) {
                Text("Leave")
            }
        }
    }
}
