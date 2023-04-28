package com.postkudigital.app.actvity.qris

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.postkudigital.app.actvity.qris.claim.RiwayatDoneFragment
import com.postkudigital.app.actvity.qris.claim.RiwayatProsesFragment

class TabClaimAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        RiwayatProsesFragment(),
        RiwayatDoneFragment()
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
            0 -> "Dalam Proses"
            else -> "Selesai"
        }
    }
}