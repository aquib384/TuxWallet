package io.tux.wallet.testnet.model.transaction.btc

data class BtcTrxHistoryModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val address: String,
        val balance: Int,
        val final_balance: Int,
        val final_n_tx: Int,
        val n_tx: Int,
        val total_received: Int,
        val total_sent: Int,
        val txs: List<Tx>,
        val unconfirmed_balance: Int,
        val unconfirmed_n_tx: Int
    ) {
        data class Tx(
            val addresses: List<String>,
            val block_hash: String,
            val block_height: Int,
            val block_index: Int,
            val confidence: Int,
            val confirmations: Int,
            val confirmed: String,
            val double_spend: Boolean,
            val fees: Int,
            val hash: String,
            val inputs: List<Input>,
            val lock_time: Int,
            val next_outputs: String,
            val outputs: List<Output>,
            val preference: String,
            val received: String,
            val relayed_by: String,
            val size: Int,
            val total: Int,
            val ver: Int,
            val vin_sz: Int,
            val vout_sz: Int,
            val vsize: Int
        ) {
            data class Input(
                val addresses: List<String>,
                val age: Int,
                val output_index: Int,
                val output_value: Int,
                val prev_hash: String,
                val script: String,
                val script_type: String,
                val sequence: Long,
                val witness: List<String>
            )

            data class Output(
                val addresses: List<String>,
                val script: String,
                val script_type: String,
                val spent_by: String,
                val value: Int
            )
        }
    }
}