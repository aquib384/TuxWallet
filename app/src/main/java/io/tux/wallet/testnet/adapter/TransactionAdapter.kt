package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemSendTransactionBinding
import io.tux.wallet.testnet.model.TransactionModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.eth.ETHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_BTC
import io.tux.wallet.testnet.utils.Constants.KEY_ETH
import io.tux.wallet.testnet.utils.Constants.KEY_LTC
import io.tux.wallet.testnet.utils.Constants.KEY_TRX
import io.tux.wallet.testnet.utils.Constants.KEY_XLM
import io.tux.wallet.testnet.utils.Utils
import java.math.MathContext.DECIMAL128
import java.math.MathContext.DECIMAL64
import kotlin.math.pow

class TransactionAdapter(var context:Context, var list: ArrayList<TransactionModel>):
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    inner class ViewHolder (var binding :ItemSendTransactionBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.ViewHolder {
        return ViewHolder(ItemSendTransactionBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        var data = list[position]
        var coin :CoinListModel  = list[position].coin
        holder.binding.tvCoin.text = coin.symbol
        val icon = context.getString(R.string.ic_prefix)+coin.symbol.lowercase()+context.getString(R.string.ic_suffix)
        holder.binding.tvCoin.setCompoundDrawablesRelativeWithIntrinsicBounds(
            Utils.getDrawableRes(context,icon),
            0,
           0,
            0
        )
        var amount =""
        holder.binding.tvTitle.text = context.getString(R.string.txn_hash)+" : ${data.hash}"
        if (data.to.isNullOrEmpty())
        {
            holder.binding.tvTo.visibility =GONE
        }
        else {
            holder.binding.tvTo.text = context.getString(R.string.to) + " : ${data.to}"
        }
        if (data.from.isNullOrEmpty())
        {
            holder.binding.tvFrom.visibility = GONE
        }
        else {
            holder.binding.tvFrom.text = context.getString(R.string.from) + " : ${data.from} "
        }
        holder.binding.tvDesc.text = Utils.createDatefromTimeStamp(data.time.toString())
        amount = when(coin.symbol) {
            KEY_ETH -> (data.amount.toDouble() / 10.0.pow(18)).toBigDecimal().toString()
            KEY_TRX -> (data.amount.toDouble() / 10.0.pow(6)).toBigDecimal().toString()
            KEY_XLM -> data.amount.toDouble().toString()
            KEY_BTC -> (data.amount.toDouble()/ 10.0.pow(8)).toBigDecimal().toString()
            KEY_LTC -> (data.amount.toDouble()/ 10.0.pow(8)).toBigDecimal().toString()
            KEY_ADA -> (data.amount.toDouble()/ 10.0.pow(6)).toBigDecimal().toString()
            else-> {
                data.amount
            }
        }
        holder.binding.tvAmount.text =amount + " "+coin.symbol

    }


    override fun getItemCount(): Int {
     return list.size
    }
}