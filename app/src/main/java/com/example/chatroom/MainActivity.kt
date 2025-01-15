package com.example.chatroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.chatroom.navigation.AppNavigation
import com.example.chatroom.utils.FileUtils

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pobieranie secretPhrase (userId) z pliku lub generowanie nowego
        val secretPhrase = FileUtils.loadOrGenerateSecretPhrase(this)

        setContent {
            // Przekazywanie secretPhrase jako userId do AppNavigation
            AppNavigation(context = this, userId = secretPhrase)
        }
    }
}
