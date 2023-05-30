package com.rinoindraw.capstonerino.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rinoindraw.capstonerino.database.model.Story
import com.rinoindraw.capstonerino.database.remotekeys.RemoteKeys
import com.rinoindraw.capstonerino.database.remotekeys.RemoteKeysDao
import com.rinoindraw.capstonerino.database.repository.StoryDao


@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryAppDatabase : RoomDatabase() {
    abstract fun getstoryDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}