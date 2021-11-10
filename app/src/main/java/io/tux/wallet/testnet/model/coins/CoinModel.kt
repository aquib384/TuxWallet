package io.tux.wallet.testnet.model.coins

import java.io.Serializable

data class CoinModel(
    val code: Int,
    val `data`: List<CoinItem>,
    val message: String
):Serializable {
    data class CoinItem(
        val coinType: String,
        val contractAddress: Any,
        val fullName: String,
        val icon: String,
        val id: Int,
        val shortName: String,
        val stakeStatus: String,
        val status: String
    )
}