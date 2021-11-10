package io.tux.wallet.testnet.model.coins

import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import io.tux.wallet.testnet.model.wallet.AdaDetails

import java.io.Serializable

class CoinListModel(
    var coin_id: Int,
    var coin_name: String,
    var symbol: String,
    var coin_add: String,
    var secret_key: String,
    var coin_key: String,
    var contract_id: String,
    var isToken: Boolean,
    var balance: String? = "0.0",
    var img: String,
    var display: Display,
    var raw: Raw,
    var adaDetail: AdaDetails,
    var isSelected: Boolean
) : Serializable {
    override fun toString(): String {
        return "CoinListModel(coin_id=$coin_id, coin_name='$coin_name', symbol='$symbol', coin_add='$coin_add', secret_key='$secret_key', coin_key='$coin_key', contract_id='$contract_id', balance='$balance', display=$display, raw=$raw, adaCoinAddressDetail=$adaDetail, isSelected=$isSelected)"
    }
}


