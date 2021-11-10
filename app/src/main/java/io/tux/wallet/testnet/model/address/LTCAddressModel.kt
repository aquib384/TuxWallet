package io.tux.wallet.testnet.model.address

data class LTCAddressModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val code: Int,
        val `data`: Data,
        val msg: String
    ) {
        data class Data(
            val address: String,
            val `private`: String,
            val `public`: String,
            val wif: String
        )
    }
}