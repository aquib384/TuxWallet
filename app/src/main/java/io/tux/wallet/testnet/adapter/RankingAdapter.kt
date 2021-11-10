package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemRankingBinding
import io.tux.wallet.testnet.model.coins.RankModel
import io.tux.wallet.testnet.utils.Utils

class RankingAdapter(var context:Context, var list: MutableList<RankModel>):
    RecyclerView.Adapter<RankingAdapter.ViewHolder>() {
    inner class ViewHolder (var binding : ItemRankingBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRankingBinding.inflate(
            LayoutInflater.from(
                context
            ), parent, false
        ))

    }




    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val display = list[position].display.currencies[0].data
        val raw = list[position].raw.currencies[0].data
        var v = display.VOLUME24HOUR.toString().split(" ")
        holder.binding.tvPair.text = raw.FROMSYMBOL.toString() +"/"+ raw.TOSYMBOL.toString()
        holder.binding.tvPrice.text = display.VOLUME24HOUR.toString().split(" ")[0]+" "+Utils.getDoubleValueFromString(raw.PRICE.toString())
        holder.binding.tvVolume.text = display.VOLUME24HOUR.toString().split(" ")[0]+" "+Utils.getDoubleValueFromString(raw.VOLUME24HOUR.toString())

        when(position%2)
        {
            0-> holder.binding.constRank.setBackgroundResource(R.drawable.card_light);
            else-> holder.binding.constRank.setBackgroundResource(R.drawable.card_dark);
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }
}