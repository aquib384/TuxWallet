package io.tux.wallet.testnet.model.wallet

import java.io.Serializable

data class AdaDetails(
    var adaCoinAddressDetails: List<CoinAddressItem.AdaCoinAddressDetail>,
    var adaMnemonic: String,
    var adaPhrase: String,
    var adaWalletId: String
):Serializable{}
