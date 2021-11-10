package io.tux.wallet.testnet.model.transaction.xlm

data class XLMTransactionHistoryModel(
    val code: Int,
    val `data`:  Data,
    val message: String
) {
    data class Data(
        val _embedded: Embedded,
        val _links: Links
    ) {
        data class Embedded(
            val records: List<Record>
        ) {
            data class Record(
                val _links: Links,
                val created_at: String?,
                val envelope_xdr: String,
                val fee_account: String,
                val fee_charged: String,
                val fee_meta_xdr: String,
                val hash: String,
                val id: String,
                val ledger: Int,
                val max_fee: String,
                val memo_type: String,
                val operation_count: Int,
                val paging_token: String,
                val result_meta_xdr: String,
                val result_xdr: String,
                val signatures: List<String>,
                val source_account: String,
                val source_account_sequence: String,
                val successful: Boolean,
                val valid_after: String
            ) {
                data class Links(
                    val account: Account,
                    val effects: Effects,
                    val ledger: Ledger,
                    val operations: Operations,
                    val precedes: Precedes,
                    val self: Self,
                    val succeeds: Succeeds,
                    val transaction: Transaction
                ) {
                    data class Account(
                        val href: String
                    )

                    data class Effects(
                        val href: String,
                        val templated: Boolean
                    )

                    data class Ledger(
                        val href: String
                    )

                    data class Operations(
                        val href: String,
                        val templated: Boolean
                    )

                    data class Precedes(
                        val href: String
                    )

                    data class Self(
                        val href: String
                    )

                    data class Succeeds(
                        val href: String
                    )

                    data class Transaction(
                        val href: String
                    )
                }
            }
        }

        data class Links(
            val next: Next,
            val prev: Prev,
            val self: Self
        ) {
            data class Next(
                val href: String
            )

            data class Prev(
                val href: String
            )

            data class Self(
                val href: String
            )
        }
    }
}