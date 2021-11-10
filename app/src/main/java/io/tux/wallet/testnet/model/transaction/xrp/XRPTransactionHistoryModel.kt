package io.tux.wallet.testnet.model.transaction.xrp

data class XRPTransactionHistoryModel(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val transactions: List<Transaction>
    ) {
        data class Transaction(
            val account: String,
            val date: String,
            val details: Details,
            val fee: Double,
            val hash: String,
            val index: Int,
            val result: String,
            val sequence: Int,
            val type: String
        ) {
            data class Details(
                val instructions: Instructions
            ) {
                data class Instructions(
                    val amount: Amount,
                    val destination: String,
                    val partial: Boolean
                ) {
                    data class Amount(
                        val amount: Int,
                        val currency: String
                    )
                }
            }
        }
    }
}