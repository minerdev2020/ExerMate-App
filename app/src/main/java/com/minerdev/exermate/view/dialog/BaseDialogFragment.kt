package com.minerdev.exermate.view.dialog

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment : DialogFragment() {
    // 다이얼로그 크기 조절 함수 (확장 함수 형식)
    protected fun Context.dialogFragmentResize(
        dialogFragment: DialogFragment,
        width: Float?,
        height: Float?
    ) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window
            val params = dialogFragment.dialog?.window?.attributes

            val x = width?.let { (size.x * it).toInt() } ?: params?.width ?: 0
            val y = height?.let { (size.y * it).toInt() } ?: params?.height ?: 0
            window?.setLayout(x, y)

        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val window = dialogFragment.dialog?.window
            val params = dialogFragment.dialog?.window?.attributes

            val x = width?.let { (rect.width() * it).toInt() } ?: params?.width ?: 0
            val y = height?.let { (rect.width() * it).toInt() } ?: params?.height ?: 0

            window?.setLayout(x, y)
        }
    }
}