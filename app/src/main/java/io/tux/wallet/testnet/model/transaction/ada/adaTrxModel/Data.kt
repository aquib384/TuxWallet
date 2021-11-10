package io.tux.wallet.testnet.model.transaction.ada.adaTrxModel

data class Data(
    val amount: Amount,
    val collateral: List<Any>,
    val deposit: Deposit,
    val depth: Depth,
    val direction: String,
    val fee: Fee,
    val id: String,
    val inputs: List<Input>,
    val inserted_at: InsertedAt,
    val metadata: Any,
    val mint: List<Any>,
    val outputs: List<Output>,
    val status: String,
    val withdrawals: List<Any>
)