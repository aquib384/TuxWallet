package io.tux.wallet.testnet.interfaces

import io.tux.wallet.testnet.model.coins.CoinListModel

interface  OnHexGeneratedInterface {
    fun onHexGenerated(coin : CoinListModel , txnHex : String)
}