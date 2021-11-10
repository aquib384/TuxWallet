package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemHomeCoinBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.DisplayCurrency
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils

class HomeCoinsAdapter(
    var context: Context,
    var list: MutableList<CoinListModel>,
    var coinInterface: CoinInterface
) :
    RecyclerView.Adapter<HomeCoinsAdapter.ViewHolder>() {
    lateinit var sharedPref: SharedPref

    inner class ViewHolder(var binding: ItemHomeCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            sharedPref = SharedPref(context)
            binding.root.setOnClickListener {
                coinInterface.coinCallBack(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCoinsAdapter.ViewHolder {
        return ViewHolder(ItemHomeCoinBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeCoinsAdapter.ViewHolder, position: Int) {
        val data = list[position]
        val display = data.display

////        if(display.coinStatus==Constants.INACTIVE) {
//            holder.itemView.visibility = View.GONE
//            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
////        }
//        else
//        {
//            holder.itemView.visibility = View.VISIBLE
//            holder.itemView.layoutParams =
//                RecyclerView.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//        }

        val currencyList: List<DisplayCurrency> = display.currencies
//        Log.e("currency",currencyList.toString())

        if (!data.isToken) {
            val ic = context.resources.getString(R.string.ic_prefix) + data.symbol.lowercase()
            holder.binding.ivIcon.setImageResource(Utils.getDrawableRes(context, ic))
        } else {
            Glide.with(context).load(data.img).into(holder.binding.ivIcon)
        }

        holder.binding.tvTitle.text = list[position].symbol


        if (currencyList.isNotEmpty()) {
            val currency = currencyList[0]
            val change = currency.data.CHANGEPCT24HOUR

            try {
                if (data.symbol == Constants.KEY_TUXC) {
                    if (sharedPref.getCurrencyCode() == "JPY") {
                        holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_yen)
                    } else {
                        holder.binding.tvPrice.text = context.getString(R.string.tuxc_price_dollar)
                    }
                    holder.itemView.visibility = View.GONE

                    holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                } else {
                    holder.binding.tvPrice.text = currency.data.PRICE.toString()
                    holder.itemView.visibility = View.VISIBLE
                    holder.itemView.layoutParams =
                        RecyclerView.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                }
                holder.binding.tvCurrency.text = currency.currency

                if (change.toString().startsWith("-")) {
                    holder.binding.tvDiff.setTextColor(
                        context.resources.getColor(
                            R.color.red,
                            context.resources.newTheme()
                        )
                    )
                    holder.binding.tvDiff.text = "$change%"
                } else {

                    holder.binding.tvDiff.setTextColor(
                        context.resources.getColor(
                            R.color.green,
                            context.resources.newTheme()
                        )
                    )
                    holder.binding.tvDiff.text = "+ $change%"
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


}
