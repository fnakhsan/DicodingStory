package com.example.dicodingstory.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dicodingstory.data.model.RemoteKeysModel
import com.example.dicodingstory.data.model.StoryModel

@Database(
    entities = [StoryModel::class, RemoteKeysModel::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java,
                        "story_db"
                    ).build()
                }
            }
            return INSTANCE as StoryDatabase
        }
    }
}