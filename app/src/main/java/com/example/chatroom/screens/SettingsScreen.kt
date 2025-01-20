package com.example.chatroom.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatroom.utils.FileUtils
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    var secretPhrase by remember { mutableStateOf("") }
    var oldSecret by remember { mutableStateOf(TextFieldValue("")) }
    var newUsername by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        secretPhrase = FileUtils.loadOrGenerateSecretPhrase(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Your Secret Phrase", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = secretPhrase.ifEmpty { "No secret phrase found" },
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                copyToClipboard(context, secretPhrase)
                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Copy to Clipboard")
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = oldSecret,
                onValueChange = { oldSecret = it },
                label = { Text("Enter old Secret Phrase") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (oldSecret.text.length == 48) {
                    secretPhrase = oldSecret.text
                    FileUtils.saveSecretPhrase(context, secretPhrase)
                    Toast.makeText(context, "Secret phrase updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid secret phrase! Must be 48 characters.", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Revert Old Account")
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Enter new username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val client = HttpClient(CIO) {
                            install(ContentNegotiation) { json() }
                        }
                        val response: HttpResponse = client.post("http://10.0.2.2:9090/changeUsername") {
                            contentType(ContentType.Application.Json)
                            setBody(ChangeUsernameRequest(secretPhrase, newUsername.text))
                        }
                        client.close()
                        if (response.status == HttpStatusCode.OK) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Failed to change username!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Change Username")
            }
        }
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clipData = android.content.ClipData.newPlainText("Secret Phrase", text)
    clipboardManager.setPrimaryClip(clipData)
}

@Serializable
data class ChangeUsernameRequest(
    val userId: String,
    val newUsername: String
)
