package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.FragmentTradeBinding


class TradeFragment : Fragment() {
  lateinit var binding : FragmentTradeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTradeBinding.inflate(layoutInflater)

        val aniSlide: Animation = AnimationUtils.loadAnimation(
           requireContext(),
            R.anim.slide_up
        )
        binding.image.startAnimation(aniSlide)
        return binding.root
    }


}