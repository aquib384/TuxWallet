package io.tux.wallet.testnet.model.transaction.btc

data class BTCHashReceiptModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
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
            val sequence: Long
        )

        data class Output(
            val addresses: List<String>,
            val script: String,
            val script_type: String,
            val value: Int
        )
    }
}