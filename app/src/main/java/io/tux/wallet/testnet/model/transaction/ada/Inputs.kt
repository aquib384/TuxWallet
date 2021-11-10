package io.tux.wallet.testnet.model.transaction.ada

data class Inputs(
    val inputs: List<Input>
) {
    data class Input(
        val address: String,
        val amount: Amount,
        val assets: List<Any>,
        val id: String,
        val index: Int
    ) {
        data class Amount(
            val quantity: Int,
            val unit: String
        )
    }
}