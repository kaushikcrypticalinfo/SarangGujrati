package com.example.saranggujrati.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.ViewUtils
import androidx.fragment.app.Fragment
import com.example.saranggujrati.ui.activity.LoginActivity
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi


lateinit var mainActivity: MainActivity



fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)

    }
}


fun snackbarWithAction(view: View,message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}




fun showSnackbar(view: View,message: String,action: (() -> Unit)? = null){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
        it
    }).show()
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}
 /*fun handleApiError(failure: Resource.Failure,view: View) {
    mainActivity = MainActivity()
    when {
        failure.isNetworkError -> {
            Snackbar.make(view,
                "Please check your internet connection",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", View.OnClickListener {
                    it
                }).show()
        }
        else -> {

            val jsonObj = JSONObject(failure.errorBody!!.charStream().readText())
            val msg = jsonObj.getString("message")

            if (msg.equals("Unauthenticated")) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
                SavedPrefrence.clearPrefrence(PerformlyApp.appContext)
                mainActivity.logout()
                mainActivity.startNewActivity(LoginActivity::class.java)
            } else
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()

        }
    }

*/



