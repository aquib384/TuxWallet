package io.tux.wallet.testnet.model.topPairResponseModel

import java.io.Serializable

data class TopPairResponse(
    val code: Int,
    val `data`: TopPairData,
    val message: String
):Serializable{}