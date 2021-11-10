package io.tux.wallet.testnet.model.createEntropy

data class EntropyData(
    val data: EntropyResponse?,
    val ltc: EntropyResponse?,
    val bch: EntropyResponse?,
    val ethereumaddress: String?,
    val ethereum_private_key: String?,
    val private_key: String,
    val wif: String?,
    val status_code: Int,
    val addresses: Addresses?,
    val address_bch: Addresses?,
    val addresses_ltc: Addresses?,
    val wif_bch: String?,
    val wif_ltc: String?,
)
