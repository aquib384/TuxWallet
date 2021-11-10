package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.databinding.ItemPhraseKeyRecoverBinding

class RecoverKeyAdapter(var context:Context): RecyclerView.Adapter<RecoverKeyAdapter.ViewHolder>()
{
    lateinit var bindingPhrase:ItemPhraseKeyRecoverBinding

    inner class ViewHolder (var binding : ItemPhraseKeyRecoverBinding):RecyclerView.ViewHolder(binding.root){
        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemPhraseKeyRecoverBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNo.text= (position+1).toString()
        bindingPhrase = holder.binding

        holder.binding.etKey.doOnTextChanged { text, start, before, count ->
            holder.binding.etKey.nextFocusForwardId
        }


    }

    override fun getItemCount(): Int {
      return  12
    }


    fun getAdapterViewBinding():ItemPhraseKeyRecoverBinding
    {
        return  bindingPhrase
    }

}