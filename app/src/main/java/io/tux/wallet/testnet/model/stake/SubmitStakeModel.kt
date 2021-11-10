package io.tux.wallet.testnet.model.stake

data class SubmitStakeModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val coin: String,
        val id: Int,
        val txId: Int,
        val walletDetailsId: Int
    )
}