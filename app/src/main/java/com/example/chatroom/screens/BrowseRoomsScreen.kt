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

    // Funkcja do pobierania listy pokojów
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
                rooms = response // Aktualizacja listy pokojów
                client.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Automatyczne odświeżanie co 3 sekundy
    LaunchedEffect(Unit) {
        while (true) {
            fetchRooms()
            delay(3000L) // 3 sekundy
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
                RoomItem(room = room, onClick = {
                    navController.navigate("chat/${room.id}") // Nawigacja do ekranu chatu
                })
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Kliknięcie prowadzi do chatu
        elevation = 4.dp
    ) {
        Text(
            text = room.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )
    }
}
