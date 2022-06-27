package com.geancarloleiva.a6_demochat.util

import android.content.Context
import android.widget.Toast

class Utils {

    companion object {
        fun showShortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}