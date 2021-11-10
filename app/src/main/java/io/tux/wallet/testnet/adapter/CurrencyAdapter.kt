package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemCurrencyBinding
import io.tux.wallet.testnet.model.CurrencyModel
import io.tux.wallet.testnet.model.coins.CoinListModel

class CurrencyAdapter(var context: Context, var currecyList :List<CurrencyModel>) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currecyList[position]
        val currency = data.currency
        holder.binding.ivIcon.setImageResource(currency.flag)
        holder.binding.tvName.text = currency.code.toString() + "(" + currency.symbol + ")"
        if(data.is_selected)
        {
            holder.binding.radioButton.setImageResource(R.drawable.radio_select)
        }
        else
        {
            holder.binding.radioButton.setImageResource(R.drawable.radio_unselect)
        }

    }

    override fun getItemCount(): Int {
        return currecyList.size
    }
 fun getClist(): List<CurrencyModel> {
        return currecyList
    }

    interface CurrencyInterface {
        fun currencyCallBack(position: Int) {}

    }

    fun updateList(list: List<CurrencyModel>) {
        this.currecyList= list
        notifyDataSetChanged()
    }

}
