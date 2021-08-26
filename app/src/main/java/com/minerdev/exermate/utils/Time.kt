package com.minerdev.exermate.utils

import android.icu.text.SimpleDateFormat
import java.util.*

object Time {
    fun getDiffTimeMsg(issuedTime: String): String {
        val sinceTime = convertStringToUnixTime(issuedTime)
        var diffTime = System.currentTimeMillis() - sinceTime

        val msg: String
        when {
            diffTime < 60 -> msg = "방금전"
            60.let { diffTime /= it; diffTime } < 60 -> msg = diffTime.toString() + "분전"
            60.let { diffTime /= it; diffTime } < 24 -> msg = diffTime.toString() + "시간전"
            24.let { diffTime /= it; diffTime } < 30 -> msg = diffTime.toString() + "일전"
            30.let { diffTime /= it; diffTime } < 12 -> msg = diffTime.toString() + "개월전"
            else -> msg = diffTime.toString() + "년전"
        }

        return msg
    }

    fun getShortDate(issuedTime: String): String {
        val sinceTime = convertStringToUnixTime(issuedTime)
        var diffTime = System.currentTimeMillis() - sinceTime

        val msg: String
        when {
            diffTime < 60 -> msg = getHMS(issuedTime)
            60.let { diffTime /= it; diffTime } < 60 -> msg = getHMS(issuedTime)
            60.let { diffTime /= it; diffTime } < 24 -> msg = getHMS(issuedTime)
            24.let { diffTime /= it; diffTime } < 30 -> msg = getYMD(issuedTime)
            30.let { diffTime /= it; diffTime } < 12 -> msg = getYMD(issuedTime)
            else -> msg = getYMD(issuedTime)
        }

        return msg
    }

    fun getDate(time: String): String {
        val date = convertStringToDate(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
        return format.toString()
    }

    fun getHMS(time: String): String {
        val date = convertStringToDate(time)
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        return format.toString()
    }

    fun getYMD(time: String): String {
        val date = convertStringToDate(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        return format.toString()
    }

    fun convertStringToDate(time: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.parse(time.replace('T', ' ').substring(0, time.length - 5))
    }

    fun convertStringToUnixTime(time: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(time.replace('T', ' ').substring(0, time.length - 5))
        return date.time
    }

    fun convertTimestampToString(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(timestamp)
    }

    fun convertTimestampToHMS(timestamp: Long): String {
        return SimpleDateFormat("a h:mm", Locale.getDefault()).format(timestamp)
    }
}