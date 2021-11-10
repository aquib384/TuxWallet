package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R

import io.tux.wallet.testnet.databinding.FragmentCurrencyBinding
import com.mynameismidori.currencypicker.ExtendedCurrency
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.activity.HomeActivity
import io.tux.wallet.testnet.adapter.CurrencyAdapter
import io.tux.wallet.testnet.model.CurrencyModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants.INR
import io.tux.wallet.testnet.utils.Utils


import io.tux.wallet.testnet.utils.RecyclerTouchListener
import io.tux.wallet.testnet.utils.SharedPref
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment : Fragment(), View.OnClickListener, CurrencyAdapter.CurrencyInterface {
  lateinit var binding :FragmentCurrencyBinding
    private var currencyList= ArrayList<CurrencyModel>()
    lateinit var currencyAdapter :CurrencyAdapter
    @Inject
    lateinit var sharedPref :SharedPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurrencyBinding.inflate(layoutInflater)
        setAdapter()
        binding.ivBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    filter(newText)
                } else currencyList.let { currencyAdapter.updateList(it) }
                return true
            }
        })
        return binding.root
    }

    private fun setAdapter()
    {
     var  currency= ExtendedCurrency.getAllCurrencies()
        Log.e("Currency",sharedPref.getCurrencyCode() +"--"+sharedPref.getCurrencySymbol())
        for (i in 0 until  currency.size)
        {
            if (currency[i].code == INR)
                currency[i].symbol = resources.getString(R.string.Rs)
//            Log.e("ccccc",currency[i].code+"---"+currency[i].symbol)
            if (currency[i].code== sharedPref.getCurrencyCode())
                currencyList.add(CurrencyModel(currency[i], true))
            else
            currencyList.add( CurrencyModel(currency[i],false))
        }

        currencyAdapter= CurrencyAdapter(requireContext(),currencyList)
        binding.recyclerview.apply {
            var linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            layoutManager = linearLayoutManager
            adapter = currencyAdapter

        }

       binding.recyclerview.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                binding.recyclerview,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val list= currencyAdapter.getClist()
                        for(i in list.indices)
                        {
                           list[i].is_selected = i == position

                        }
                        currencyAdapter.notifyItemChanged(position)
                        currencyAdapter.notifyDataSetChanged()
                        saveCurrency(list[position])
//                        Utils.showToast(requireContext(), currencyList[position].currency.symbol)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                }
            ))

    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_back ->findNavController().popBackStack()
            R.id.btn_save ->{
                currencyList.forEach {
                    if (it.is_selected)
                    {
                        saveCurrency(it)
                    }
                }
            }
        }
    }


    private fun saveCurrency(currency :CurrencyModel)
    {
        Log.e("selected curr",currency.currency.code)
        sharedPref.setCurrencyCode(currency.currency.code)
        sharedPref.setCurrencySymbol(currency.currency.symbol)
            Utils.showToast(requireContext(),resources.getString(R.string.curreny_updated))
        findNavController().popBackStack()
        (activity as HomeActivity?)?.recreate()
    }
    fun filter(text: String?) {
        val temp: ArrayList<CurrencyModel> = ArrayList()
        for (country in currencyList) {
            if (text?.let { country.currency.name.startsWith(it, true) } == true ||
                text?.let { country.currency.code.startsWith(it, true) } == true
            ) {
                temp.add(country)
            }
        }
       currencyAdapter.updateList(temp)
        currencyAdapter.notifyDataSetChanged()
    }

}