package io.tux.wallet.testnet.model.wallet

data class RecoverWalletResponseModel(
    var code: Int,
    var `data`: Data,
    var message: String
) {
    data class Data(
        val deviceId: String,
        val id: Int,
        val lockType: String,
        val mnemonics: String,
        val roles: List<Role>,
        val coinAddressDetails: List<WalletAddresses>,
        val walletPin: String
    ) {
        data class Role(
            val description: String,
            val id: Int,
            val name: String,
            val privileges: List<Any>,
            val representationName: String
        )

        data class WalletAddresses(
            val address: String,
            val coin: String,
            val contractId: String,
            val deviceId: String,
            val id: Int,
            val privateKey: String,
            val userDetailsId: Int
        )
    }
}