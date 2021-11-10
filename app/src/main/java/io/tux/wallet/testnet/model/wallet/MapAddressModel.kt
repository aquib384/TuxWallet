package io.tux.wallet.testnet.model.wallet

data class MapAddressModel(
    val code: Int,
    val `data`: List<CoinAddressItem>,
    val message: String
) {

}