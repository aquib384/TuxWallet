package io.tux.wallet.testnet.model

data class UpdateTxCountModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val endDate: String,
        val id: Int,
        val stakeCounter: Any,
        val startDate: String,
        val txCounter: Any
    )
}