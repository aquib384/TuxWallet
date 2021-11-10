package io.tux.wallet.testnet.model

data class SaveFcmStatusResponse(
    val code: Int,
    val `data`: Data?,
    val message: String
) {
    data class Data(
        val deviceId: String,
        val fcmStatus: String?,
        val id: Int,
        val msg: String,
        val tokenId: String,
        val userDetailsId: Int
    )
}