package com.example.chatroom.utils

import android.content.Context
import java.io.File
import java.util.Base64

object FileUtils {
    private const val FILE_NAME = "secret_phrase.txt"

    // Generowanie losowej frazy (base58, 48 znaków)
    fun generateSecretPhrase(): String {
        val chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz" // Base58
        return (1..48).map { chars.random() }.joinToString("")
    }

    // Zapisywanie frazy do pliku
    fun saveSecretPhrase(context: Context, secretPhrase: String) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(secretPhrase)
    }

    // Ładowanie frazy z pliku lub generowanie nowej, jeśli plik nie istnieje
    fun loadOrGenerateSecretPhrase(context: Context): String {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            file.readText()
        } else {
            val newPhrase = generateSecretPhrase()
            saveSecretPhrase(context, newPhrase)
            newPhrase
        }
    }
}
