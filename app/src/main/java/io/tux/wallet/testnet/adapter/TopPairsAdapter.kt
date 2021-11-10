package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemRankingBinding
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import io.tux.wallet.testnet.model.topPairResponseModel.TopPairDataItem
import io.tux.wallet.testnet.utils.Utils

class TopPairsAdapter(var context:Context, var list: MutableList<TopPairDataItem>):
    RecyclerView.Adapter<TopPairsAdapter.ViewHolder>() {
    inner class ViewHolder (var binding : ItemRankingBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRankingBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                ))

        }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

                holder.binding.tvPair.text = data.fromSymbol+"/"+ data.toSymbol
//                holder.binding.tvPrice.text = " "+Utils.getDoubleValueFromString( context,data.price.toString())
                holder.binding.tvVolume.text = Utils.getDoubleValueFromString(data.volume24h.toString())

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