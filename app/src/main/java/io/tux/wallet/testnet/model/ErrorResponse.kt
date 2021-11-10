package io.tux.wallet.testnet.model

data class ErrorResponse(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    class Data
}