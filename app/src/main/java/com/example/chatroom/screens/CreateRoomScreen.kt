package com.example.chatroom.screens

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.serialization.kotlinx.json.*
import com.example.chatroom.utils.FileUtils

@Serializable
data class CreateRoomRequest(val roomName: String, val password: String?, val userId: String)

fun createRoomRequest(context: Context, roomName: String, password: String, callback: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val userId = FileUtils.loadOrGenerateSecretPhrase(context) // Załaduj lub wygeneruj userId
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
            val response: HttpResponse = client.post("http://10.0.2.2:9090/createRoom") {
                contentType(ContentType.Application.Json)
                setBody(CreateRoomRequest(roomName, password, userId))
            }
            val responseBody = response.bodyAsText()
            client.close()
            callback(responseBody)
        } catch (e: Exception) {
            callback("Error: ${e.localizedMessage}")
        }
    }
}

@Composable
fun CreateRoomScreen(navController: NavController) {
    val context = LocalContext.current // Pobierz kontekst
    var roomName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var serverResponse by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Room Name Input
        TextField(
            value = roomName,
            onValueChange = { roomName = it },
            label = { Text("Room Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Optional Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to Create Room
        Button(
            onClick = {
                createRoomRequest(context, roomName, password) { response ->
                    serverResponse = response
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Room")
        }

        // Display Server Response
        Spacer(modifier = Modifier.height(16.dp))
        if (serverResponse.isNotEmpty()) {
            Text(
                text = "Server Response: $serverResponse",
                style = MaterialTheme.typography.body1
            )
        }
    }
}
