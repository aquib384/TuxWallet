package io.tux.wallet.testnet.model.transaction.btc

data class BTCTransactionHistoryModel(
    val code: Long,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val address: String,
        val balance: Long,
        val final_balance: Long,
        val final_n_tx: Long,
        val hasMore: Boolean,
        val n_tx: Long,
        val total_received: Long,
        val total_sent: Long,
        val txs: List<Tx>?,
        val unconfirmed_balance: Long,
        val unconfirmed_n_tx: Long
    ) {
        data class Tx(
            val addresses: List<String>,
            val block_hash: String,
            val block_height: Long,
            val block_index: Long,
            val confidence: Long,
            val confirmations: Long,
            val confirmed: String,
            val double_spend: Boolean,
            val fees: Long,
            val hash: String,
            val inputs: List<Input>,
            val lock_time: Long,
            val next_outputs: String,
            val opt_in_rbf: Boolean,
            val outputs: List<Output>?,
            val preference: String,
            val received: String,
            val relayed_by: String,
            val size: Long,
            val total: Long,
            val ver: Long,
            val vin_sz: Long,
            val vout_sz: Long,
            val vsize: Long
        ) {
            data class Input(
                val addresses: List<String>?,
                val age: Long,
                val output_index: Long,
                val output_value: Long,
                val prev_hash: String,
                val script: String,
                val script_type: String,
                val sequence: Long,
                val witness: List<String>
            )

            data class Output(
                val addresses: List<String>?,
                val script: String,
                val script_type: String,
                val spent_by: String,
                val value: Long
            )
        }
    }
}