package com.app.shrimadbhagavatgita.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.shrimadbhagavatgita.datasource.api.ApiUtilities
import com.app.shrimadbhagavatgita.datasource.api.room.SavedChapters
import com.app.shrimadbhagavatgita.datasource.api.room.SavedChaptersDao
import com.app.shrimadbhagavatgita.datasource.api.room.SavedVerses
import com.app.shrimadbhagavatgita.datasource.api.room.SavedVersesDao
import com.app.shrimadbhagavatgita.datasource.sharedprefer.SharedPreferencesManager
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.models.VersesItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(val savedChaptersDao: SavedChaptersDao, val savedVersesDao: SavedVersesDao, val sharedPreferencesManager: SharedPreferencesManager) {

    fun getAllChapters(): Flow<List<ChaptersItem>> = callbackFlow {
        val callback = object  :  Callback<List<ChaptersItem>>{
            override fun onResponse(
                call: Call<List<ChaptersItem>>,
                response: Response<List<ChaptersItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<ChaptersItem>>, t: Throwable) {
               close(t)
            }

        }

        ApiUtilities.api.getAllChapters().enqueue(callback)
        awaitClose {  }
    }

    fun getVerses(chapterNumber : Int): Flow<List<VersesItem>> = callbackFlow {
        val callback = object  :  Callback<List<VersesItem>>{
            override fun onResponse(
                call: Call<List<VersesItem>>,
                response: Response<List<VersesItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<VersesItem>>, t: Throwable) {
                close(t)
            }

        }

        ApiUtilities.api.getVerses(chapterNumber).enqueue(callback)
        awaitClose {  }
    }

    fun getAParticularVerse(chapterNumber : Int,verseNumber : Int, ): Flow<VersesItem> = callbackFlow {
        val callback = object  :  Callback<VersesItem>{
            override fun onResponse(call: Call<VersesItem>, response: Response<VersesItem>) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<VersesItem>, t: Throwable) {
                close(t)
            }



        }

        ApiUtilities.api.getAParticularVerse(chapterNumber, verseNumber).enqueue(callback)
        awaitClose {  }
    }

    //saved Chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()

    fun getAParticularChapter(chapter_number : Int) : LiveData<SavedChapters> = savedChaptersDao.getAParticularChapter(chapter_number)

    suspend fun deleteChapter(id : Int) = savedChaptersDao.deleteChapter(id)

    //saved Verses

    suspend fun insertEnglishVerse(versesInEnglish: SavedVerses) = savedVersesDao.insertEnglishVerse(versesInEnglish)

    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = savedVersesDao.getAllEnglishVerse()

    fun getParticularVerse(chapterNumber: Int , verseNumber: Int) : LiveData<SavedVerses> = savedVersesDao.getParticularVerse(chapterNumber, verseNumber)

    suspend fun deleteAParticularVerse(chapterNumber: Int , verseNumber: Int) =savedVersesDao.deleteAParticularVerse(chapterNumber, verseNumber)

    //sharedPreferences
    fun getAllSavedChaptersKey(): Set<String> = sharedPreferencesManager.getAllSavedChaptersKey()
    fun putSavedChaptersSP(key: String, value : Int) = sharedPreferencesManager.putSavedChaptersSP(key,value)
    fun deleteSavedChaptersSP(key: String) = sharedPreferencesManager.deleteSavedChaptersSP(key)

}






























