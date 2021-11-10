package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemNotificationBinding
import io.tux.wallet.testnet.model.NotificationModel


class NotificationAdapter(var context:Context, var list: ArrayList<NotificationModel>):
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    lateinit var constraintLayout: ConstraintLayout
    inner class ViewHolder (var binding :ItemNotificationBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        var data = list[position]

        holder.binding.tvTitle.text = data.title.toString()
        holder.binding.tvDesc.text = data.desc.toString()
        holder.binding.tvDate.text = data.date.toString()
        if (data.type == "request")
        {
            holder.binding.btnAccept.visibility = VISIBLE
            holder.binding.btnDecline.visibility = VISIBLE
        }
        else
        {
            holder.binding.btnAccept.visibility = GONE
            holder.binding.btnDecline.visibility = GONE
        }

        when(position%2)
        {
            0-> holder.binding.constNotification.setBackgroundResource(R.drawable.card_light);
            else-> holder.binding.constNotification.setBackgroundResource(R.drawable.card_dark);
        }




    }


    override fun getItemCount(): Int {
     return list.size
    }
}