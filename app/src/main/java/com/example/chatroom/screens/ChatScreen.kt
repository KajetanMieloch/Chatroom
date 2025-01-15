package com.example.chatroom.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatroom.models.SendMessageRequest
import com.example.chatroom.models.Message
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import io.ktor.client.call.body

@Composable
fun ChatScreen(navController: NavController, roomId: String, context: Context, userId: String) {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    fun fetchMessages() {
        coroutineScope.launch {
            try {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                val response: List<Message> = client.get("http://10.0.2.2:9090/getMessages") {
                    parameter("roomId", roomId)
                }.body()
                messages = response
                client.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage() {
        coroutineScope.launch {
            try {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                client.post("http://10.0.2.2:9090/sendMessage") {
                    contentType(ContentType.Application.Json)
                    setBody(SendMessageRequest(roomId, message, userId))
                }
                client.close()
                message = ""
                fetchMessages()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            fetchMessages()
            delay(3000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { msg ->
                val isCurrentUser = msg.userId == userId
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .widthIn(max = 300.dp)
                            .wrapContentWidth(if (isCurrentUser) Alignment.End else Alignment.Start)
                    ) {
                        Text(
                            text = msg.nickname ?: "Anonim",
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.secondary
                        )
                        Surface(
                            color = if (isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            shape = MaterialTheme.shapes.medium,
                            elevation = 2.dp
                        ) {
                            Text(
                                text = msg.content,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(8.dp),
                                textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Enter your message") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { sendMessage() }) {
                Text("Send")
            }
        }
    }
}
