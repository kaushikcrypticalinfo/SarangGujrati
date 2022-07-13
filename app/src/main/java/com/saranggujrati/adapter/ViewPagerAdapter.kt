package com.saranggujrati.adapter

import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*


class ViewPagerAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(
    manager!!
) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add("")
    }

    fun setFragment(index: Int, fragment: Fragment) {
        mFragmentList[index] = fragment
    }

    fun remove(index: Int) {
        mFragmentList.removeAt(index)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
    fun detectSwipeToRight(event: MotionEvent): Boolean {
        val initialXValue = 0 // as we have to detect swipe to right
        val SWIPE_THRESHOLD = 100 // detect swipe
        var result = false
        try {
            val diffX = event.x - initialXValue
            if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                result = if (diffX > 0) {
                    // swipe from left to right detected ie.SwipeRight
                    false
                } else {
                    // swipe from right to left detected ie.SwipeLeft
                    false
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return result
    }


    }