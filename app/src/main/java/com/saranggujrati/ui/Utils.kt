package com.saranggujrati.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.Display
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.utils.RSS_FEED_DATE_FORMAT
import com.saranggujrati.utils.RSS_FEED_DATE_FORMAT_GMT
import com.saranggujrati.utils.RSS_FEED_DATE_FORMAT_T
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


lateinit var mainActivity: MainActivity


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}
@Throws(Exception::class)
fun parseDate(strDate: String?): Date? {
    if (strDate != null && !strDate.isEmpty()) {
        val formats = arrayOf(
            SimpleDateFormat(RSS_FEED_DATE_FORMAT),
            SimpleDateFormat(RSS_FEED_DATE_FORMAT_GMT),
            SimpleDateFormat(RSS_FEED_DATE_FORMAT_T)
        )
        var parsedDate: Date? = null
        for (i in formats.indices) {
            return try {
                parsedDate = formats[i].parse(strDate)
                parsedDate
            } catch (e: ParseException) {
                continue
            }
        }
    }
    throw Exception("Unknown date format: '$strDate'")
}


fun snackbarWithAction(view: View, message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun isValidEmail(target: CharSequence?): Boolean {
    return if (target == null) false else Patterns.EMAIL_ADDRESS.matcher(target).matches()
}


fun sendMail(context: Context, subject: String, body: String) {
    val selectorIntent = Intent(Intent.ACTION_SENDTO)
    selectorIntent.data = Uri.parse("mailto:")
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("brandcoremedia@gmail.com"))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_TEXT, body)
    emailIntent.selector = selectorIntent
    context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
}

fun showSnackbar(view: View, message: String, action: (() -> Unit)? = null) {
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



