package ru.practicum.android.diploma.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(private val context: Context, private val dbName: String, version: Int) :
    SQLiteOpenHelper(context, dbName, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        // USE Room
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // USE Room
    }

    fun deleteBadDatabase() {
        this.close()
        SQLiteDatabase.deleteDatabase(
            context.getDatabasePath(dbName)
        )
    }
}
