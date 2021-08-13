package com.minerdev.exermate.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

object LoadImage {
    private val imageCache = HashMap<String, Bitmap>()

    fun get(urlStr: String): Bitmap? {
        return if (imageCache.containsKey(urlStr)) {
            imageCache[urlStr]

        } else {
            val url = URL(urlStr)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            imageCache[urlStr] = bitmap
            bitmap
        }
    }
}