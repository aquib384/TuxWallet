package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemCoinPerformanceBinding
import io.tux.wallet.testnet.model.CoinPerformanceModel

class CoinPerformanceAdapter(var context:Context, var list: ArrayList<CoinPerformanceModel>):
    RecyclerView.Adapter<CoinPerformanceAdapter.ViewHolder>() {
    inner class ViewHolder (var binding : ItemCoinPerformanceBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCoinPerformanceBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                ))

        }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

                holder.binding.tvPair.text = data.title
                holder.binding.tvPrice.text = data.value

        when(data.type)
        {
           "high" ->{
               holder.binding.tvPrice.setTextColor(context.resources.getColor(R.color.green))
           }
            "low" ->{
               holder.binding.tvPrice.setTextColor(context.resources.getColor(R.color.red))
           }
        }


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