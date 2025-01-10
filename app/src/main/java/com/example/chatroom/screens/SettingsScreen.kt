package com.example.chatroom.screens

import android.content.ClipData
import android.content.ClipboardManager
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
import com.example.chatroom.MainActivity
import com.example.chatroom.utils.FileUtils

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    var oldSecret by remember { mutableStateOf(TextFieldValue("")) } // Pole na starą frazę

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                    }
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
                text = MainActivity.secretPhrase,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                copyToClipboard(context, MainActivity.secretPhrase)
                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Copy to Clipboard")
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Pole tekstowe na starą frazę
            TextField(
                value = oldSecret,
                onValueChange = { oldSecret = it },
                label = { Text("Enter old Secret Phrase") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Przycisk do zmiany frazy
            Button(onClick = {
                if (oldSecret.text.length == 48) {
                    // Zapisanie nowej frazy
                    MainActivity.secretPhrase = oldSecret.text
                    FileUtils.saveSecretPhrase(context, MainActivity.secretPhrase)
                    Toast.makeText(context, "Secret phrase updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid secret phrase! Must be 48 characters.", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Revert Old Account")
            }
        }
    }
}

// Funkcja kopiowania frazy do schowka
fun copyToClipboard(context: Context, text: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Secret Phrase", text)
    clipboardManager.setPrimaryClip(clipData)
}
