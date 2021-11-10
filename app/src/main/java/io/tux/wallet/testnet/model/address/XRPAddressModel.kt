package io.tux.wallet.testnet.model.address

data class XRPAddressModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val `data`: Data,
        val responseCode: Int,
        val responseMessage: String
    ) {
        data class Data(
            val address: String,
            val classicAddress: String,
            val secret: String,
            val xAddress: String
        )
    }
}