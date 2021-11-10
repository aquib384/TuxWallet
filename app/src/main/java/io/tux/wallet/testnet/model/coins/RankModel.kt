package io.tux.wallet.testnet.model.coins

import android.view.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import java.io.Serializable

data class RankModel(var display: io.tux.wallet.testnet.model.coinResponseModel.Display, var raw: Raw):Serializable {
}