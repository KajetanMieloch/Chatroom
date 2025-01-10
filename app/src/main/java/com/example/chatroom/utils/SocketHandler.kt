package com.example.chatroom.utils

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketHandler {
    private lateinit var socket: Socket

    fun connect() {
        socket = IO.socket("http://srv29.mikr.us:10385")
        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun joinRoom(roomId: String) {
        socket.emit("joinRoom", JSONObject().put("roomId", roomId))
    }

    fun createRoom(name: String, password: String) {
        socket.emit("createRoom", JSONObject().put("name", name).put("password", password))
    }

    fun sendMessage(message: String) {
        socket.emit("message", message)
    }

    fun listenForMessages(onMessageReceived: (String) -> Unit) {
        socket.on("message") { args ->
            if (args.isNotEmpty()) {
                onMessageReceived(args[0] as String)
            }
        }
    }
}
