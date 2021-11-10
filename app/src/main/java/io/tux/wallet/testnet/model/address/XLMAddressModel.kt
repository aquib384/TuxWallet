package io.tux.wallet.testnet.model.address

data class XLMAddressModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val publicKey: String,
        val secretKey: String
    )
}