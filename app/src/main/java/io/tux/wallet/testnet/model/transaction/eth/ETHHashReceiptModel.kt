package io.tux.wallet.testnet.model.transaction.eth

data class ETHHashReceiptModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val blockHash: String,
        val blockNumber: String,
        val contractAddress: Any?,
        val cumulativeGasUsed: String,
        val effectiveGasPrice: String,
        val from: String,
        val gasUsed: String,
        val logs: List<Any>,
        val logsBloom: String,
        val status: String,
        val to: String,
        val transactionHash: String,
        val transactionIndex: String
    )
}