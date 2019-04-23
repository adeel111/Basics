package com.example.adeeliftikhar.messenger.Models

class ChatMessageModel(
    val messageID: String,
    val text: String,
    val fromID: String,
    val toID: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "", -1)
}