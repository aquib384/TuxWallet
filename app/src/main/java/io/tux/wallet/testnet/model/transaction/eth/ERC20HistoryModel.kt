package io.tux.wallet.testnet.model.transaction.eth

data class ERC20HistoryModel(
    val code: Int,
    val `data`: List<Data>?,
    val message: String
) {
    data class Data(
        val blockHash: String,
        val blockNumber: String,
        val confirmations: String,
        val contractAddress: String,
        val cumulativeGasUsed: String,
        val from: String,
        val gas: String,
        val gasPrice: String,
        val gasUsed: String,
        val hash: String,
        val input: String,
        val nonce: String,
        val timeStamp: String,
        val to: String,
        val tokenDecimal: String,
        val tokenName: String,
        val tokenSymbol: String,
        val transactionIndex: String,
        val value: String
    )
}
