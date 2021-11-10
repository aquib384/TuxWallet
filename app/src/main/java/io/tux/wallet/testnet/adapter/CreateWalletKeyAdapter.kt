package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.databinding.ItemPhraseKeyNewBinding


class CreateWalletKeyAdapter(var context:Context,var keysList: ArrayList<String>): RecyclerView.Adapter<CreateWalletKeyAdapter.ViewHolder>()
{
    inner class ViewHolder (var binding : ItemPhraseKeyNewBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemPhraseKeyNewBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNo.text= (position+1).toString()
        holder.binding.etKey.text= keysList[position].toString()

    }

    override fun getItemCount(): Int {
      return  keysList.size
    }


}