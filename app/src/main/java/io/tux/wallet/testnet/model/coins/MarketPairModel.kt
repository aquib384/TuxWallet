package io.tux.wallet.testnet.model.coins

import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import java.io.Serializable

class MarketPairModel(
    var display: Display,
    var raw: Raw,
): Serializable {
}