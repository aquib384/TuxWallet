package io.tux.wallet.testnet.model

import io.tux.wallet.testnet.model.coins.CoinListModel

class TransactionModel(

    var coin :CoinListModel,
    var time :Long,
    var amount :String,
    var to :String,
    var from :String,
    var hash :String,
    var blockNumber:String


) {
}