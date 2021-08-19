package com.minerdev.exermate.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "chatDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table chatUsers(id integer primary key autoincrement, roomId integer, userId integer, email text, nickname text, profileUrl text);")
        db?.execSQL("create table chatLogs(id integer primary key autoincrement, roomId integer, fromId integer, text text, type integer, createdAt integer);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists chatUsers;")
        db?.execSQL("drop table if exists chatLogs;")
        onCreate(db)
    }
}