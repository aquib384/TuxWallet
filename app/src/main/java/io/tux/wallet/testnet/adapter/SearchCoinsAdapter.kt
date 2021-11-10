package io.tux.wallet.testnet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemSearchBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.DisplayCurrency
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils

class SearchCoinsAdapter(
    var context: Context,
    var list: List<CoinListModel>,
    var coinInterface: CoinInterface
) :
    RecyclerView.Adapter<SearchCoinsAdapter.ViewHolder>() {
    lateinit var cFilteredList: List<CoinListModel>
    lateinit var sharedPref : SharedPref
    inner class ViewHolder(var binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            sharedPref= SharedPref(context)
            cFilteredList = list
            binding.root.setOnClickListener {
                coinInterface.coinCallBack(adapterPosition)
            }
        }

    }


    fun getFilteredList(): List<CoinListModel> {
        return list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchCoinsAdapter.ViewHolder, position: Int) {
        val data = list[position]
        var display = data.display
        Log.e("display", display.toString())
//        if(display.coinStatus.equals(Constants.INACTIVE)) {
//            holder.itemView.visibility = View.GONE
//            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
//        }
//        else
//        {
//            holder.itemView.visibility = View.VISIBLE
//            holder.itemView.layoutParams =
//                RecyclerView.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//        }
        val currencyList: List<DisplayCurrency> = display.currencies
        Log.e("currency", currencyList.toString())
        val currency = currencyList[0]
        if (!data.isToken) {
            val ic = context.resources.getString(R.string.ic_prefix) + data.symbol.lowercase()
            holder.binding.ivIcon.setImageResource(Utils.getDrawableRes(context, ic))
        }
        else{
            Glide.with(context).load(data.img).into(holder.binding.ivIcon)
        }
        try {
            holder.binding.tvTitle.text = data.symbol
            holder.binding.tvDesc.text = data.coin_name
            if (data.symbol== Constants.KEY_TUXC)
            {
                if (sharedPref.getCurrencyCode()=="JPY")
                {
                    holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_yen)
                }
                else {
                    holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_dollar)
                }
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
            else {
                holder.binding.tvPrice.text = currency.data.PRICE.toString()
                holder.itemView.visibility = View.VISIBLE
                holder.itemView.layoutParams =
                    RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            }
            holder.binding.tvAmount.text = data.balance?.let { Utils.getDoubleValueFromString(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        when(position%2)
//        {
//            0-> holder.binding.constWallet.setBackgroundResource(R.drawable.card_light)
//            else-> holder.binding.constWallet.setBackgroundResource(R.drawable.card_dark)
//        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<CoinListModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}