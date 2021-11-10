package io.tux.wallet.testnet.model.transaction.xrp

data class XRPHashReceiptModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val raw: Raw,
        val summary: Summary
    ) {
        data class Raw(
            val date: String,
            val hash: String,
            val ledger_index: Int,
            val meta: Meta,
            val tx: Tx
        ) {
            data class Meta(
                val AffectedNodes: List<AffectedNode>,
                val TransactionIndex: Int,
                val TransactionResult: String,
                val delivered_amount: String
            ) {
                data class AffectedNode(
                    val ModifiedNode: ModifiedNodes
                ) {
                    data class ModifiedNodes(
                        val FinalFields: FFields,
                        val LedgerEntryType: String,
                        val LedgerIndex: String,
                        val PreviousFields: PrevFields,
                        val PreviousTxnID: String,
                        val PreviousTxnLgrSeq: Int
                    ) {
                        data class FFields(
                            val Account: String,
                            val Balance: String,
                            val Flags: Int,
                            val OwnerCount: Int,
                            val Sequence: Int
                        )

                        data class PrevFields(
                            val Balance: String,
                            val Sequence: Int
                        )
                    }
                }
            }

            data class Tx(
                val Account: String,
                val Amount: String,
                val Destination: String,
                val DestinationTag: Int,
                val Fee: String,
                val Flags: Long,
                val LastLedgerSequence: Int,
                val Sequence: Int,
                val SigningPubKey: String,
                val TransactionType: String,
                val TxnSignature: String,
                val date: String
            )
        }

        data class Summary(
            val instructions: Instructions
        ) {
            data class Instructions(
                val amount: Amount,
                val destination: String,
                val partial: Boolean
            ) {
                data class Amount(
                    val amount: Int,
                    val currency: String
                )
            }
        }
    }
}