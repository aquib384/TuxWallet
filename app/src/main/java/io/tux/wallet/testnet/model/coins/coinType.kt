package io.tux.wallet.testnet.model.coins

data class coinType(
    val coinType: String,
    val contractAddress: Any,
    val fullName: String,
    val icon: String,
    val id: Int,
    val shortName: String,
    val stakeStatus: String,
    val status: String
)