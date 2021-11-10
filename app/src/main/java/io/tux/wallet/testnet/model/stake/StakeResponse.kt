package io.tux.wallet.testnet.model.stake

data class StakeResponse(
    val code: Int,
    val `data`: Data?,
    val msg: String?
)
{
    data class Data(
        val amount: Int,
        val memo: String,
        val tx_id: String,
        val type: String,
        val voter_address: String
    )
}