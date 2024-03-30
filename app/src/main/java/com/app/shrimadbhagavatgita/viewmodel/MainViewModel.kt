package com.app.shrimadbhagavatgita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.shrimadbhagavatgita.datasource.api.room.AppDatabase
import com.app.shrimadbhagavatgita.datasource.api.room.SavedChapters
import com.app.shrimadbhagavatgita.datasource.api.room.SavedVerses
import com.app.shrimadbhagavatgita.datasource.sharedprefer.SharedPreferencesManager
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.models.VersesItem
import com.app.shrimadbhagavatgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application): AndroidViewModel(application) {
    val savedChaptersDao = AppDatabase.getDatabaseInstance(application)?.savedChaptersDao()
    val savedVersessDao = AppDatabase.getDatabaseInstance(application)?.savedVersesDao()

    val sharedPreferencesManager = SharedPreferencesManager(application)

    val appRepository = AppRepository(savedChaptersDao!!, savedVersessDao!!, sharedPreferencesManager)
    fun getAllChapters(): Flow<List<ChaptersItem>> = appRepository.getAllChapters()

    fun getVerses(chapterNumber : Int): Flow<List<VersesItem>> =appRepository.getVerses(chapterNumber)

    fun getAParticularVerse(chapterNumber : Int,verseNumber : Int, ): Flow<VersesItem> =appRepository.getAParticularVerse(chapterNumber, verseNumber)

    //saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = appRepository.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = appRepository.getSavedChapters()

    fun getAParticularChapter(chapter_number : Int) : LiveData<SavedChapters> = appRepository.getAParticularChapter(chapter_number)

    suspend fun deleteChapter(id : Int) = appRepository.deleteChapter(id)

    //saved verses

    suspend fun insertEnglishVerse(versesInEnglish: SavedVerses) = appRepository.insertEnglishVerse(versesInEnglish)

    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = appRepository.getAllEnglishVerse()

    fun getParticularVerse(chapterNumber: Int , verseNumber: Int) : LiveData<SavedVerses> = appRepository.getParticularVerse(chapterNumber, verseNumber)

    suspend fun deleteAParticularVerse(chapterNumber: Int , verseNumber: Int) =appRepository.deleteAParticularVerse(chapterNumber, verseNumber)

    //sharedPreferences
    fun getAllSavedChaptersKey(): Set<String> = appRepository.getAllSavedChaptersKey()
    fun putSavedChaptersSP(key: String, value : Int) = appRepository.putSavedChaptersSP(key,value)
    fun deleteSavedChaptersSP(key: String) = appRepository.deleteSavedChaptersSP(key)


}















