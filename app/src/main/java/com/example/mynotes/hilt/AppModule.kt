package com.example.mynotes.hilt

import android.content.Context
import androidx.room.Room
import com.example.mynotes.data.AppRepositoryImpl
import com.example.mynotes.domain.AppRepository
import com.example.mynotes.room.AppDao
import com.example.mynotes.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        var INSTANCE: AppDatabase? = null

        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(context, AppDatabase::class.java, "notes_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            return instance
        }
    }

    @Provides
    @Singleton
    fun provideAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.getDao()
    }

    @Provides
    @Singleton
    fun provideAppRepository(dao: AppDao): AppRepository {
        return AppRepositoryImpl(dao)
    }
}