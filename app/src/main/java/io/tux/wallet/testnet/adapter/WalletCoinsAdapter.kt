package io.tux.wallet.testnet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemWalletBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.DisplayCurrency
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants.KEY_TUXC
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils

class WalletCoinsAdapter(
    var context: Context,
    var list: List<CoinListModel>,
    var coinInterface: CoinInterface
) :

    RecyclerView.Adapter<WalletCoinsAdapter.ViewHolder>() {
    lateinit var sharedPref: SharedPref

    inner class ViewHolder(var binding: ItemWalletBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            sharedPref = SharedPref(context)
            binding.root.setOnClickListener { coinInterface.coinCallBack(adapterPosition) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWalletBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WalletCoinsAdapter.ViewHolder, position: Int) {
        val data = list[position]
        Log.e("coindata", "onBindViewHolder: ${Gson().toJson(list[1])}")
        try {
            var display = data.display
//        Log.e("display",display.toString())
            var currencyList: List<DisplayCurrency> = display.currencies
            if (!data.isToken) {
                val ic = context.resources.getString(R.string.ic_prefix) + data.symbol.lowercase()
                holder.binding.ivIcon.setImageResource(Utils.getDrawableRes(context, ic))
            } else {
                Glide.with(context).load(data.img).into(holder.binding.ivIcon)
            }

            holder.binding.tvTitle.text = data.symbol
            holder.binding.tvDesc.text = data.coin_name
            if (currencyList.isNotEmpty()) {
                val currency = currencyList[0]
                if (data.symbol == KEY_TUXC) {
                    if (sharedPref.getCurrencyCode() == "JPY") {
                        holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_yen)
                    } else {
                        holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_dollar)
                    }
                } else {
                    holder.binding.tvPrice.text = currency.data.PRICE.toString()
                }
            }

            val balance =
                if (data.balance?.toDouble()!! > 0) Utils.getDoubleValueFromString6(data.balance!!) else "0.000000"

            holder.binding.tvAmount.text = balance
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (position % 2) {
            0 -> setDarkBackground(position, holder.binding.constWallet)

            else -> setLightBackground(position, holder.binding.constWallet)
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }


    private fun setDarkBackground(pos: Int, view: View) {
        when (pos) {
            0 -> view.setBackgroundResource(R.drawable.card_dark_top_rounded)
            list.size - 1 -> view.setBackgroundResource(R.drawable.card_dark_bottom_rounded)
            else -> view.setBackgroundResource(R.drawable.card_dark)
        }
    }

    private fun setLightBackground(pos: Int, view: View) {
        when (pos) {
            0 -> view.setBackgroundResource(R.drawable.card_light_top_rounded)
            list.size - 1 -> view.setBackgroundResource(R.drawable.card_light_bottom_rounded)
            else -> view.setBackgroundResource(R.drawable.card_light)
        }
    }
}