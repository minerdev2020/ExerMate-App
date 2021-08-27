package com.minerdev.exermate.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "exermate", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table walkRecords(id integer primary key autoincrement, createdAt integer, stepCount integer);")
        db?.execSQL("create table joinedChatRooms(id integer primary key autoincrement, roomId text, name text);")
        db?.execSQL("create table chatMembers(id integer primary key autoincrement, roomId text, userId text, email text, nickname text, profileUrl text);")
        db?.execSQL("create table chatLogs(id integer primary key autoincrement, roomId text, fromId text, createdAt integer, text text, type integer);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists walkRecords;")
        db?.execSQL("drop table if exists joinedChatRooms;")
        db?.execSQL("drop table if exists chatMembers;")
        db?.execSQL("drop table if exists chatLogs;")
        onCreate(db)
    }
}