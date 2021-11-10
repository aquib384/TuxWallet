package io.tux.wallet.testnet.model.coins

data class BalanceResponseModel(
    val code: Int,
    val `data`: Data?,
    val message: String
) {
    data class Data(
        val balance: Any?
    )
}