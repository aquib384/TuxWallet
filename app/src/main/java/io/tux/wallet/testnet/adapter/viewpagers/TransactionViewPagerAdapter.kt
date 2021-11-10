package io.tux.wallet.testnet.adapter.viewpagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.tux.wallet.testnet.fragment.transaction.TransactionReceiveFragment
import io.tux.wallet.testnet.fragment.transaction.TransactionSentFragment

class TransactionViewPagerAdapter(fa: FragmentActivity?) : FragmentStateAdapter(fa!!) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TransactionSentFragment()
            else -> {
                TransactionReceiveFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}