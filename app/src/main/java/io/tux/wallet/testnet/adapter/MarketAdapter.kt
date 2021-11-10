package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemMarketBinding
import io.tux.wallet.testnet.model.coins.MarketPairModel
import io.tux.wallet.testnet.utils.Utils

class MarketAdapter(
    var context: Context,
    var list: ArrayList<MarketPairModel>
) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    inner class MarketViewHolder(var binding: ItemMarketBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MarketViewHolder(
        ItemMarketBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MarketAdapter.MarketViewHolder, position: Int) {
        val rData = list[position].raw.currencies[0].data
        val dData = list[position].display.currencies[0].data

        val change = rData.CHANGEPCT24HOUR
        try {
            holder.binding.tvName.text =
                rData?.FROMSYMBOL.toString() + "/" + rData?.TOSYMBOL.toString()
            holder.binding.tvVolume.text =
                Utils.getDoubleValueFromString(rData?.VOLUME24HOUR.toString())
//                data?.vOLUME24HOUR.toString()
//                data?.vOLUME24HOUR?.let { Utils.getDoubleValueFromDouble(context, it.toString()) }

            holder.binding.tvOldPrice.text = dData.TOSYMBOL.toString() + " " + rData?.PRICE?.let {
                Utils.getDoubleValueFromString(it.toString())
            }
            holder.binding.tvNewPrice.text = dData.TOSYMBOL.toString() + " " + rData?.PRICE?.let {
                Utils.getDoubleValueFromString(it.toString())
            }
//                Utils.getDoubleValueFromString(rData?.CHANGE24HOUR.toString())

            if (change.toString().startsWith("-")) {
                holder.binding.btnChange.text =
                    change?.let { Utils.getDoubleValueFromString(it.toString()) } + "%"
                holder.binding.btnChange.setBackgroundColor(context.resources.getColor(R.color.red))
                holder.binding.tvNewPrice.setTextColor(context.resources.getColor(R.color.red))
            } else {
                holder.binding.btnChange.text =
                    "+" + change?.let { Utils.getDoubleValueFromString(it.toString()) } + "%"
                holder.binding.btnChange.setBackgroundColor(context.resources.getColor(R.color.green))
                holder.binding.tvNewPrice.setTextColor(context.resources.getColor(R.color.green))

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}