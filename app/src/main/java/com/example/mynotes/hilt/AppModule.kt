package com.example.mynotes.hilt

import android.content.Context
import androidx.room.Room
import com.example.mynotes.data.AppRepositoryImpl
import com.example.mynotes.data.TrashRepositoryImpl
import com.example.mynotes.domain.AppRepository
import com.example.mynotes.domain.TrashRepository
import com.example.mynotes.room.NotesDao
import com.example.mynotes.room.AppDatabase
import com.example.mynotes.room.TrashDao
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
    fun provideAppDao(appDatabase: AppDatabase): NotesDao {
        return appDatabase.getNotesDao()
    }

    @Provides
    @Singleton
    fun provideTrashDao(appDatabase: AppDatabase): TrashDao {
        return appDatabase.getTrashDao()
    }

    @Provides
    @Singleton
    fun provideTrashRepository(trashDao: TrashDao): TrashRepository {
        return TrashRepositoryImpl(trashDao)
    }
    @Provides
    @Singleton
    fun provideAppRepository(dao: NotesDao): AppRepository {
        return AppRepositoryImpl(dao)
    }
}