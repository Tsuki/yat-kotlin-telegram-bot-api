package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a phone contact.
 *
 * @property photoNumber Contact's phone number.
 * @property firstName Contact's first name.
 * @property lastName Contact's last name.
 * @property userId Contact's user identifier in Telegram.
 */
data class Contact(
        @com.google.gson.annotations.SerializedName("photo_number") val photoNumber: String,
        @com.google.gson.annotations.SerializedName("first_name") val firstName: String,
        @com.google.gson.annotations.SerializedName("last_name") val lastName: String?,
        @com.google.gson.annotations.SerializedName("user_id") val userId: Long?)