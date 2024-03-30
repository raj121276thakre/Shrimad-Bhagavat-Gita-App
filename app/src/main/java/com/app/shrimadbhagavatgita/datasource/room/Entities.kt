package com.app.shrimadbhagavatgita.datasource.api.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.shrimadbhagavatgita.models.Commentary
import com.app.shrimadbhagavatgita.models.Translation


@Entity(tableName = "SavedChapters")
data class SavedChapters(
    val chapter_number: Int,
    val chapter_summary: String,
    val chapter_summary_hindi: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val name_meaning: String,
    val name_translated: String,
    val name_transliterated: String,
    val slug: String,
    val verses_count: Int,
    val verses: List<String>
)

@Entity(tableName = "SavedVerses")
data class SavedVerses(
    val chapter_number: Int,
    val commentaries: List<Commentary>,
    @PrimaryKey
    val id: Int,
    val slug: String,
    val text: String,
    val translations: List<Translation>,
    val transliteration: String,
    val verse_number: Int,
    val word_meanings: String
)







