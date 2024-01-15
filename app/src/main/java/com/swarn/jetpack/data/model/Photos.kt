package com.swarn.jetpack.data.model


import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("total_photos")
    val totalPhotos: Int
) {
    data class Photo(
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("user")
        val user: Int
    )
}