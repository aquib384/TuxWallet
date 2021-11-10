package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.databinding.ItemAnswerBinding


class AnswerOptionsKeyAdapter(var context:Context, var keysList: MutableList<String> ,var answerInterface: AnswerInterface): RecyclerView.Adapter<AnswerOptionsKeyAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                answerInterface.onOptionSelectedCallBack(adapterPosition,binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAnswerBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNo.text = keysList[position]


    }

    override fun getItemCount(): Int {
        return keysList.size
    }


    interface AnswerInterface {
        fun onOptionSelectedCallBack(position: Int , binding:ItemAnswerBinding) {}

    }
}