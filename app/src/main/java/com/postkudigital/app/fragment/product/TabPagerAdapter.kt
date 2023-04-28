package com.postkudigital.app.fragment.product

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.postkudigital.app.fragment.product.kategori.KategoriFragment
import com.postkudigital.app.fragment.product.menu.MenuFragment
import com.postkudigital.app.fragment.product.stok.StockFragment

class TabPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        MenuFragment(),
        KategoriFragment(),
        StockFragment()
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
            0 -> "Menu"
            1 -> "Kategori"
            else -> "Stock"
        }
    }
}