package com.rinoindraw.capstonerino.network

import android.content.Context
import androidx.room.Room
import com.rinoindraw.capstonerino.database.database.StoryAppDatabase
import com.rinoindraw.capstonerino.database.remotekeys.RemoteKeysDao
import com.rinoindraw.capstonerino.database.repository.StoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    fun provideStoriesDao(storyDatabase: StoryAppDatabase): StoryDao = storyDatabase.getstoryDao()


    @Provides
    fun provideRemoteKeysStoryDao(storyDatabase: StoryAppDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideStoriesDatabase(@ApplicationContext context: Context): StoryAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryAppDatabase::class.java,
            "stories_database"
        ).build()
    }
}