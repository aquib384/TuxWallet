package io.tux.wallet.testnet.adapter.viewpagers

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.databinding.ItemWelcomePagesBinding
import io.tux.wallet.testnet.model.WelcomePagerModel

class WelcomePagerAdapter
    (
    private var context: Context,
    private var list: ArrayList<WelcomePagerModel>):RecyclerView.Adapter<WelcomePagerAdapter.PagerViewHolder>(){

    class PagerViewHolder (val binding: ItemWelcomePagesBinding):
    RecyclerView.ViewHolder(binding.root);



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemLayoutBinding = ItemWelcomePagesBinding.inflate(
            LayoutInflater.from(context), parent, false)

        return PagerViewHolder(itemLayoutBinding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {

         holder.binding.ivImage.setImageResource(list[position].imageDrawable)
        holder.binding.tvTitle.text = list[position].title
        holder.binding.tvDesc.text = list[position].desc

    }

    override fun getItemCount(): Int {
        return list.size
    }
}