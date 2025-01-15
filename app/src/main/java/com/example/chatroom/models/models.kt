package com.example.chatroom.models

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val roomId: String,
    val message: String,
    val userId: String
)

@Serializable
data class Message(
    val userId: String,
    val nickname: String?,
    val content: String
)
