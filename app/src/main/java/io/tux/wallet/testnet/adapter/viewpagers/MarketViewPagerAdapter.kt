package io.tux.wallet.testnet.adapter.viewpagers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.tux.wallet.testnet.fragment.MarketListFragment

class MarketViewPagerAdapter(fa: Fragment?, var tabList: List<String>) :
    FragmentStateAdapter(fa!!) {
    override fun createFragment(position: Int): Fragment {
        return MarketListFragment.newInstance(tabList[position])
    }

    override fun getItemCount(): Int {
        return tabList.size
    }
}