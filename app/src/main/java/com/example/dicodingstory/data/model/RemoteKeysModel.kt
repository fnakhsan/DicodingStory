package com.example.dicodingstory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysModel(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)