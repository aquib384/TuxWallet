package io.tux.wallet.testnet.model.transaction.ada.adaTrxModel

data class AdaTrxResponse(
    val code: Int,
    val `data`: List<Data>?,
    val message: String?
)