package io.tux.wallet.testnet.adapter.viewpagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.tux.wallet.testnet.fragment.transaction.TrxHistoryListFragment
import io.tux.wallet.testnet.model.coins.CoinListModel


class TrxHistoryViewPagerAdapter(fa: FragmentActivity?, var coin :CoinListModel) : FragmentStateAdapter(fa!!)
{
    override fun createFragment(position: Int): Fragment {
        return TrxHistoryListFragment.newInstance(coin,position)

    }

    override fun getItemCount(): Int {
        return 2
    }
}