package com.geancarloleiva.a6_demochat.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Utils {

    companion object {
        fun showShortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun hideKeyboard(context: Context, activity: AppCompatActivity){
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(inputManager.isAcceptingText){
                inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }
    }
}