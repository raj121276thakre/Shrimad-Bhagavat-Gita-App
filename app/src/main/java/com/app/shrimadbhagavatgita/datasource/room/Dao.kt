package com.app.shrimadbhagavatgita.datasource.api.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChaptersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(savedChapters: SavedChapters)

    @Query("SELECT * FROM SavedChapters")
    fun getSavedChapters() : LiveData<List<SavedChapters>>

    @Query("DELETE FROM SavedChapters WHERE id = :id")
    suspend fun deleteChapter(id : Int)

    @Query("SELECT * FROM SavedChapters WHERE chapter_number = :chapter_number")
    fun getAParticularChapter(chapter_number : Int) : LiveData<SavedChapters>
}

@Dao
interface SavedVersesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnglishVerse(versesInEnglish: SavedVerses)

    @Query("SELECT * FROM SavedVerses")
    fun getAllEnglishVerse() : LiveData<List<SavedVerses>>

    @Query("SELECT * FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number = :verseNumber")
    fun getParticularVerse(chapterNumber: Int , verseNumber: Int) : LiveData<SavedVerses>

    @Query("DELETE FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number = :verseNumber")
    suspend fun deleteAParticularVerse(chapterNumber: Int , verseNumber: Int)


}
























