package com.example.saranggujrati.widget

import android.content.Context
import android.opengl.ETC1
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable

import androidx.viewpager.widget.ViewPager
import android.opengl.ETC1.getHeight

import android.opengl.ETC1.getWidth

import android.view.MotionEvent
import com.example.saranggujrati.AppClass

class VerticalViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    /*fun VerticalViewPager(context: Context?) {
        this(context, null)
    }


    fun VerticalViewPager(context: Context?, attrs: AttributeSet?) {
        super(context, attrs)
        init()
    }*/

    init {
        init()
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollHorizontally(direction)
    }


    private fun init() {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = OVER_SCROLL_NEVER
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val toIntercept = super.onInterceptTouchEvent(flipXY(ev!!))
        flipXY(ev)
        return toIntercept
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val toHandle = super.onTouchEvent(flipXY(ev!!))
        flipXY(ev)
        return toHandle
    }

    private fun flipXY(ev: MotionEvent): MotionEvent? {
        val width = width.toFloat()
        val height = height.toFloat()
        val x = ev.y / height * width
        val y = ev.x / width * height
        ev.setLocation(x, y)
        return ev
    }

}
private class VerticalPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        if (position < -1) {
            view.alpha = 0f
        } else if (position <= 1) {
            view.alpha = 1f
            view.translationX = pageWidth * -position
            val yPosition = position * pageHeight
            view.translationY = yPosition
        } else {
            view.alpha = 0f
        }
    }
}

   /* constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            when {
                position < -1 -> view.alpha = 0f
                position <= 1 -> {
                    view.alpha = 1f
                    view.translationX = view.width * -position
                    val yPosition = position * view.height
                    view.translationY = yPosition
                }
                else -> view.alpha = 0f
            }
        }
    }

    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev)
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

}*/