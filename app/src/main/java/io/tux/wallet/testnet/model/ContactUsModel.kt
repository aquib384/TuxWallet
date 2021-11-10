package io.tux.wallet.testnet.model

data class ContactUsModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val description: String,
        val emailId: String,
        val fullName: String,
        val id: Int,
        val subject: String
    )
}