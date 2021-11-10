package io.tux.wallet.testnet.model

data class LanguageDetectModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val language: String
    )
}