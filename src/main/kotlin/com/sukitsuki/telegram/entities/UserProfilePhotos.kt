package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represent a user's profile pictures.
 *
 * @property totalCount Total number of profile pictures the target user has.
 * @property photos Requested profile pictures (in up to 4 sizes each).
 */
data class UserProfilePhotos(
        @Name("total_count") val totalCount: Int,
        val photos: List<List<PhotoSize>>)