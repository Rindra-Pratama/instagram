package com.pmob.baseproj5

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "caption")
    val caption: String,

    @ColumnInfo(name = "image_uri")
    val imageUri: String // URI gambar disimpan sebagai String
)