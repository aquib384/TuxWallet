package io.tux.wallet.testnet.model.transaction.eth

data class ETHHexToHashModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val error: Error,
        val result: String,
        val id: Int,
        val jsonrpc: String
    ) {
        data class Error(
            val code: Int,
            val message: String
        )
    }
}