package io.tux.wallet.testnet.model.stake

data class StakesHistoryModel(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val coin: String,
        val createAt: List<Int>,
        val date: Any?,
        val id: Int,
        val txId: String,
        val walletDetailsId: Int
    )
}