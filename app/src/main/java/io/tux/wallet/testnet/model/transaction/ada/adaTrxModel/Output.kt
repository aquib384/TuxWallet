package io.tux.wallet.testnet.model.transaction.ada.adaTrxModel

data class Output(
    val address: String,
    val amount: AmountXX,
    val assets: List<Any>
)