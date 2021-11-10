package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemOrderHistoryBinding
import io.tux.wallet.testnet.model.OrderHistoryModel

class OrderHistoryAdapter(var context:Context, var list: ArrayList<OrderHistoryModel>):
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    inner class ViewHolder (var binding : ItemOrderHistoryBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderHistoryBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                ))

        }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

                holder.binding.tvPair.text = data.pair
                holder.binding.tvPrice.text = data.amount
                holder.binding.tvVolume.text = data.total_order

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