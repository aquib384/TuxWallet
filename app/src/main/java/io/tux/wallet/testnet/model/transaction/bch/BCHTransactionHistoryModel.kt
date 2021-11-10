package io.tux.wallet.testnet.model.transaction.bch

data class BCHTransactionHistoryModel(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val meta: Meta,
        val payload: List<Payload>
    ) {
        data class Meta(
            val index: Int,
            val limit: Int,
            val results: Int,
            val totalCount: Int
        )

        data class Payload(
            val blockhash: String,
            val blockheight: Int,
            val blocktime: String,
            val confirmations: Int,
            val datetime: String,
            val fee: String,
            val hash: String,
            val index: Int,
            val locktime: Int,
            val size: Int,
            val time: String,
            val timestamp: Int,
            val txid: String,
            val txins: List<Txin>,
            val txouts: List<Txout>,
            val version: Int
        ) {
            data class Txin(
                val addresses: List<String>,
                val amount: String,
                val script: Script,
                val sequence: Long,
                val txout: String,
                val votype: String,
                val vout: Int
            ) {
                data class Script(
                    val asm: String,
                    val hex: String
                )
            }

            data class Txout(
                val addresses: List<String>,
                val amount: String,
                val script: Script,
                val spent: Boolean,
                val type: String
            ) {
                data class Script(
                    val asm: String,
                    val hex: String,
                    val reqsigs: Int
                )
            }
        }
    }
}