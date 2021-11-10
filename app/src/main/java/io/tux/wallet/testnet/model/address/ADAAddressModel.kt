package io.tux.wallet.testnet.model.address

data class ADAAddressModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val address: List<Addres>,
        val id: String,
        val mnemonic: List<String>,
        val passphrase: String
    ) {
        data class Addres(
            val derivation_path: List<String>,
            val id: String,
            val state: String
        )
    }

}