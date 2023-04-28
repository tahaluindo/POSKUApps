package com.postkudigital.app.actvity.wallet.topup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabTopupAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        TopupProsesFragment(),
        TopupSuksesFragment(),
        TopupCancelFragment()
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
            0 -> "Proses"
            1 -> "Sukses"
            else -> "Batal"
        }
    }
}