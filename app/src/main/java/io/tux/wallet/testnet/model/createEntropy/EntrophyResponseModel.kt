package io.tux.wallet.testnet.model.createEntropy

data class EntrophyResponseModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val addresses: Addresses,
        val entropy: String,
        val ethereum_private_key: String,
        val ethereumaddress: String,
        val private_key: String,
        val status_code: Int
    ) {
        data class Addresses(
            val p2pkh: String,
            val p2sh: String,
            val p2wpkh: String,
            val p2wpkh_in_p2sh: String,
            val p2wsh: String,
            val p2wsh_in_p2sh: String
        )
    }
}