package com.example.saranggujrati.extensions

import android.os.Build
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.saranggujrati.R
import timber.log.Timber

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Daniel Frimpong on February 05, 2022.
 * https://github.com/kakyire
 */


fun ViewGroup.createItemView(
    @LayoutRes layout: Int
): View {
    return LayoutInflater.from(context)
        .inflate(layout, this, false)

}

fun TextView.setPubDate(date: String) {
    try {
//        Tue, 25 Jan 2022 13:31:35
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val inputDateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.getDefault())
        SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss Z", Locale.getDefault())
        val time = inputDateFormat.parse(date).time
        val now = Calendar.getInstance().timeInMillis
        val timeAgo = DateUtils.getRelativeTimeSpanString(
            time,
            now,
            DateUtils.SECOND_IN_MILLIS
        )
            .toString()

        text = timeAgo
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.setThumbnail(imageUrl: String) {
    visibility = View.VISIBLE
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(this)

}


fun TextView.formatHtmlText(value: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(value)
    }
}


/**
 * Load more new items when user scroll the list end
 * @param fetchMoreNews lambda function to trigger pagination
 */
fun RecyclerView.paginateNews(fetchMoreNews: () -> Unit) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val totalItemCount = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            if (visibleItems + lastVisibleItem > totalItemCount) {
                fetchMoreNews()
            }

            Log.d(
                "TAG", """onScrolled:
                    | Total items: $totalItemCount
                    | Visible items: $visibleItems
                    | Last item: $lastVisibleItem
                """.trimMargin()
            )
        }
    })

}