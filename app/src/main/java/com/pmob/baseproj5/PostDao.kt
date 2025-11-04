package com.pmob.baseproj5

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(post: Post)

    @Update
    fun update(post: Post)

    @Delete
    fun delete(post: Post)

    @Query("SELECT * from post ORDER BY id DESC")
    fun getAllPost(): LiveData<List<Post>>

    @Query("SELECT * FROM post WHERE id = :postId")
    fun getPostById(postId: Int): Post
}