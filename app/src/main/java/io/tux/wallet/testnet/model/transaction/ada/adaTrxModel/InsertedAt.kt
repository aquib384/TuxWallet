package io.tux.wallet.testnet.model.transaction.ada.adaTrxModel

data class InsertedAt(
    val absolute_slot_number: Int,
    val epoch_number: Int,
    val height: Height,
    val slot_number: Int,
    val time: String
)