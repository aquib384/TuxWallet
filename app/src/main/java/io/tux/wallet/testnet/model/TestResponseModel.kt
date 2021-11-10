package io.tux.wallet.testnet.model

data class TestResponseModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val `data`: Data,
        val isSuccessful: Boolean,
        val message: String,
        val status: Int
    ) {
        data class Data(
            val txHash: String
        )
    }
}