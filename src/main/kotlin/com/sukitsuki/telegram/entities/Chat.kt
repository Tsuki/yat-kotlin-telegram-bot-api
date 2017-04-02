package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a chat.
 *
 * @property id Unique identifier for this chat.
 * @property type Type of chat, can be either “private”, “group”, “supergroup” or “channel”.
 * @property title Title, for channels and group chats.
 * @property userName Username, for private chats, supergroups and channels if available.
 * @property firstName First name of the other party in a private chat.
 * @property lastName Last name of the other party in a private chat.
 */
data class Chat(
        val id: Long,
        val type: String,
        val title: String?,
        @com.google.gson.annotations.SerializedName("username") val userName: String?,
        @com.google.gson.annotations.SerializedName("first_name") val firstName: String?,
        @com.google.gson.annotations.SerializedName("last_name") val lastName: String?)