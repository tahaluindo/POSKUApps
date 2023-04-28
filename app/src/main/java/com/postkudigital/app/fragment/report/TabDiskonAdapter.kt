package com.postkudigital.app.fragment.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabDiskonAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val pages = listOf(
        RangkingTransFragment(),
        RankingItemFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Transaksi"
            else -> "Item"
        }
    }
}