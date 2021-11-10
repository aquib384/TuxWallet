package io.tux.wallet.testnet.model.transaction.ada.adaTrxModel

data class Input(
    val address: String?,
    val amount: AmountX,
    val assets: List<Any>,
    val id: String?,
    val index: Int
)