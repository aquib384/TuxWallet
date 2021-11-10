package io.tux.wallet.testnet.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.WelcomeActivity
import io.tux.wallet.testnet.databinding.FragmentAccountBinding
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils


class AccountFragment : Fragment(), View.OnClickListener {
    lateinit var binding : FragmentAccountBinding
    lateinit var sharedPref:SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater)
        sharedPref = SharedPref(context)
        binding.cardSetting.setOnClickListener(this)
        binding.cardContact.setOnClickListener(this)
        setAnimations()
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {

            R.id.card_setting->{
                findNavController().navigate(R.id.navigation_settings)
            }
            R.id.card_contact->{
                findNavController().navigate(R.id.navigation_contact)
            }
        }
    }


    private fun setAnimations()
    {
        val animationSlide = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fall_down)
       binding.cardContact.startAnimation(animationSlide)
       binding.cardSetting.startAnimation(animationSlide)

    }

}