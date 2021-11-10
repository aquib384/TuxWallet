package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.LayoutCodePickerBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.KEY_TUXC
import io.tux.wallet.testnet.utils.Utils

class CoinsAdapter(var context:Context, var list: List<CoinListModel>, var coinInterface: CoinInterface , var isSend :Boolean):
    RecyclerView.Adapter<CoinsAdapter.ViewHolder>() {


    inner class ViewHolder (var binding : LayoutCodePickerBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{

               for(element in list)
               {
                   element.isSelected = element==list[adapterPosition]

               }
                coinInterface.coinCallBack(adapterPosition)
        }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutCodePickerBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                ))

        }




    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = list[position]
        if(isSend)
        {
            if (data.symbol==KEY_TUXC)
            {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
            else
            {
               holder.itemView.visibility = View.VISIBLE
               holder.itemView.layoutParams =
                    RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

            }
        }
//        if(!isStake) {
//           setVisbility(data.display.coinStatus,holder.itemView)
//        }
//        else
//        {
//          setVisbility(data.display.stakeStatus,holder.itemView)
//        }

            val ic = context.resources.getString(R.string.ic_prefix)+data.symbol.lowercase()
            holder.binding.textViewName.text = data.coin_name+"("+ data.symbol+")"
        if (!data.isToken) {
            val ic = context.resources.getString(R.string.ic_prefix) + data.symbol.lowercase()
            holder.binding.imageViewFlag.setImageResource(Utils.getDrawableRes(context, ic))
        }
        else{
            Glide.with(context).load(data.img).into(holder.binding.imageViewFlag)
        }
            if(data.isSelected)
            {
                holder.binding.radioButton.setImageResource(R.drawable.radio_select)
            }
        else
            {
                holder.binding.radioButton.setImageResource(R.drawable.radio_unselect)
            }



    }


    override fun getItemCount(): Int {
     return list.size
    }


//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(charSequence: CharSequence): FilterResults {
//                val charString = charSequence.toString()
//                if (charString.isEmpty()) {
//                    cFilteredList = list as ArrayList<CountryModel>
//                } else {
//                    val filteredList: MutableList<CountryModel> = ArrayList()
//                    for (row in list) {
//
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (row.name.toLowerCase()
//                                .contains(charString.toLowerCase()) || row.code
//                                .contains(charSequence)
//                            || row.country_code
//                                .contains(charSequence)
//                        ) {
//                            filteredList.add(row)
//                        }
//                    }
//                    cFilteredList = filteredList as ArrayList<CountryModel>
//                }
//                val filterResults = FilterResults()
//                filterResults.values = cFilteredList
//                return filterResults
//            }
//
//            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
//               list = filterResults.values as ArrayList<CountryModel>
//                notifyDataSetChanged()
//            }
//        }
//    }



    private fun setVisbility(key:String, view: View)
    {
        if(key == Constants.ACTIVE) {
            view.visibility = View.VISIBLE
            view.layoutParams =
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

        }
        else
        {
            view.visibility = View.GONE
            view.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

}


