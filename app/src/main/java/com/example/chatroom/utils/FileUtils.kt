package com.example.chatroom.utils

import android.content.Context
import java.io.File

object FileUtils {
    private const val FILE_NAME = "secret_phrase.txt"

    fun generateSecretPhrase(): String {
        val chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz" // Base58
        return (1..48).map { chars.random() }.joinToString("")
    }

    fun saveSecretPhrase(context: Context, secretPhrase: String) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(secretPhrase)
    }

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
