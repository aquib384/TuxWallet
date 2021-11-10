package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.StakesAdapter
import io.tux.wallet.testnet.databinding.FragmentStakeBinding
import io.tux.wallet.testnet.model.stake.StakesHistoryModel
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.StakesViewModel
import javax.inject.Inject

@AndroidEntryPoint
class StakeFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentStakeBinding
    var list = ArrayList<StakesHistoryModel.Data>()

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var stakesAdapter: StakesAdapter
    private val viewModel: StakesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStakeBinding.inflate(layoutInflater)
     //   Utils.showComingSoonDialog(requireContext())
        binding.btnStake.setOnClickListener(this)
        binding.btnStakeGo.setOnClickListener(this)
        viewModel.getStakeHistory(sharedPref.getWalletId().toString())
        setStakes()

        return binding.root
    }


    private fun setStakes() {

        viewModel.stakesHistoryModel.observe(viewLifecycleOwner, {

            if (it.data == null) {
                binding.tvNoData.visibility = VISIBLE
            } else {
                binding.tvNoData.visibility = GONE

                stakesAdapter = StakesAdapter(
                    requireContext(),
                    it.data as ArrayList<StakesHistoryModel.Data>
                )
                binding.rvRanking.apply {
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.orientation = RecyclerView.VERTICAL
                    layoutManager = linearLayoutManager
                    adapter = stakesAdapter

                }
            }


        })
//        loadingBar.dismiss()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_stake -> findNavController().navigate(R.id.navigation_buy_stakes)
           R.id.btn_stake_go -> findNavController().navigate(R.id.navigation_buy_stakes)
           // R.id.btn_stake -> Utils.showComingSoonDialog(requireContext())
           // R.id.btn_stake_go -> Utils.showComingSoonDialog(requireContext())
        }
    }

}