package io.tux.wallet.testnet.fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import io.tux.wallet.testnet.databinding.FragmentEmailVerifiedBinding


class EmailVerifiedFragment : Fragment() {
    lateinit var binding : FragmentEmailVerifiedBinding
    var from :String =""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailVerifiedBinding.inflate(layoutInflater)
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }

        return binding.root

    }



}