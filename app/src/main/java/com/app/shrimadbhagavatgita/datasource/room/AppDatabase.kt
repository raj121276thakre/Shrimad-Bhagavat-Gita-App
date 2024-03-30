package com.app.shrimadbhagavatgita.datasource.api.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedChapters::class, SavedVerses::class], version = 2, exportSchema = false)
@androidx.room.TypeConverters(AppTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun savedChaptersDao(): SavedChaptersDao
    abstract fun savedVersesDao(): SavedVersesDao


   companion object{
       @Volatile
       var INSTANCE : AppDatabase? = null

       fun getDatabaseInstance(context: Context): AppDatabase?{
           val tempInstance = INSTANCE
           if (INSTANCE!= null) return  tempInstance
           synchronized(this){
               val roomDb = Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").fallbackToDestructiveMigration().build()
               INSTANCE = roomDb
               return roomDb
           }
       }

   }

}







































