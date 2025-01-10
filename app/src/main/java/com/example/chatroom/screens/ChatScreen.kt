package com.example.chatroom.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatroom.utils.SocketHandler

@Composable
fun ChatScreen(navController: NavController, roomId: String?) {
    var messages = remember { mutableStateListOf<String>() }
    var inputMessage = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        SocketHandler.connect() // Dodajemy nawiązanie połączenia
        SocketHandler.joinRoom(roomId ?: "")
        SocketHandler.listenForMessages { message ->
            messages.add(message)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages.size) { index ->
                Text(messages[index], modifier = Modifier.padding(8.dp)) // Poprawny import Text
            }
        }
        Row(modifier = Modifier.padding(16.dp)) {
            BasicTextField(
                value = inputMessage.value,
                onValueChange = { inputMessage.value = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(onClick = {
                SocketHandler.sendMessage(inputMessage.value)
                inputMessage.value = ""
            }) {
                Text("Send") // Poprawny import Text
            }
        }
    }
}
