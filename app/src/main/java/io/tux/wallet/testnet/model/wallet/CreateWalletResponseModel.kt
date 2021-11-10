package io.tux.wallet.testnet.model.wallet

data class CreateWalletResponseModel(
    val code: Int,
    val `data`: Data,
    val message: String

) {
    data class Data(
        val id: Int,
        val mnemonics: String,
        val role: String,
        val walletPin: String,
        val lockType: String
    )


}