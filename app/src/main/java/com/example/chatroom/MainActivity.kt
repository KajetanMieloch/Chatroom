package com.example.chatroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.chatroom.navigation.AppNavigation
import com.example.chatroom.utils.FileUtils

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var secretPhrase: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ładowanie lub generowanie secret phrase
        secretPhrase = FileUtils.loadOrGenerateSecretPhrase(this)

        setContent {
            AppNavigation()
        }
    }
}
