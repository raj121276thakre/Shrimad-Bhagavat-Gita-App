package com.app.shrimadbhagavatgita.datasource.sharedprefer

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(val context: Context) {

    val sharedPreferences : SharedPreferences by lazy {
        context.getSharedPreferences("saved_chapters", Context.MODE_PRIVATE)
    }

    fun getAllSavedChaptersKey(): Set<String> {
       return sharedPreferences.all.keys
    }

    fun putSavedChaptersSP(key: String, value : Int) {
         sharedPreferences.edit().putInt(key,value).apply()
    }

    fun deleteSavedChaptersSP(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}














