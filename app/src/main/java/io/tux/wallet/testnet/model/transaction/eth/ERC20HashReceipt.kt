package io.tux.wallet.testnet.model.transaction.eth

data class ERC20HashReceipt(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val blockHash: String,
        val blockNumber: String,
        val contractAddress: Any,
        val cumulativeGasUsed: String,
        val effectiveGasPrice: String,
        val from: String,
        val gasUsed: String,
        val logs: List<Log>,
        val logsBloom: String,
        val status: String,
        val to: String,
        val transactionHash: String,
        val transactionIndex: String,
        val type: String
    ) {
        data class Log(
            val address: String,
            val blockHash: String,
            val blockNumber: String,
            val `data`: String,
            val logIndex: String,
            val removed: Boolean,
            val topics: List<String>,
            val transactionHash: String,
            val transactionIndex: String
        )
    }
}