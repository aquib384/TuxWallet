package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemStakeBinding

import io.tux.wallet.testnet.model.stake.StakesHistoryModel
import io.tux.wallet.testnet.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StakesAdapter(var context:Context, var list: ArrayList<StakesHistoryModel.Data>):
    RecyclerView.Adapter<StakesAdapter.ViewHolder>() {
    inner class ViewHolder (var binding : ItemStakeBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStakeBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                ))

        }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]
            var dateList :ArrayList<Int> = data.createAt as ArrayList<Int>
        var d = ""
        d = if(dateList[2].toString().length==1) {
            "0"+dateList[2].toString()+"/"+dateList[1].toString()+"/"+dateList[0].toString()
        } else {
            dateList[2].toString() + "/" + dateList[1].toString() + "/" + dateList[0].toString()
        }
       var originalFormat =  SimpleDateFormat("dd/MM/yyyy");
        var targetFormat =  SimpleDateFormat("dd MMM yyyy");
        try {


            var date = originalFormat.parse(d);
            var formattedd = targetFormat.format(date)
            holder.binding.tvPair.text = data.coin
            holder.binding.tvVolume.text = formattedd
            holder.binding.tvPrice.text = data.txId
        }
        catch (e:Exception)
        {e.printStackTrace()}

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