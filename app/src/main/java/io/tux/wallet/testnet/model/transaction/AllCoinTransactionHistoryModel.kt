//package io.tux.wallet.testnet.model.transaction
//
//data class AllCoinTransactionHistoryModel(
//    val code: Int,
//    val `data`: Data,
//    val message: String
//) {
//    data class Data(
//        val ADA: ADA,
//        val BCH: List<BCH>,
//        val BTC: List<BTC>,
//        val ETH: List<ETH>,
//        val LTC: List<LTC>,
//        val NEO: List<NEO>,
//        val TRX: List<TRX>,
////        val XLM: XLM
//    ) {
//        data class ADA(
//            val address: String,
//            val advance: Any?,
//            val offset: Any?
//        )
//
//        data class BCH(
//            val context: Context,
//            val `data`: Data
//        ) {
//            data class Context(
//                val api: Api,
//                val cache: Cache,
//                val code: Int,
//                val full_time: Double,
//                val limit: String,
//                val market_price_usd: Double,
//                val offset: String,
//                val render_time: Double,
//                val request_cost: Int,
//                val results: Int,
//                val server: String,
//                val source: String,
//                val state: Int,
//                val time: Double
//            ) {
//                data class Api(
//                    val documentation: String,
//                    val last_major_update: String,
//                    val next_major_update: Any?,
//                    val notice: String,
//                    val version: String
//                )
//
//                data class Cache(
//                    val duration: Int,
//                    val live: Boolean,
//                    val since: String,
//                    val time: Any?,
//                    val until: String
//                )
//            }
//
//            data class Data(
//                val bitcoincash:qr2g2rgvgmdcgg44hpxllzjexm37mj754qt047sel3: BitcoincashQr2g2rgvgmdcgg44hpxllzjexm37mj754qt047sel3
//            ) {
//                data class BitcoincashQr2g2rgvgmdcgg44hpxllzjexm37mj754qt047sel3(
//                    val address: Address,
//                    val transactions: List<Any>,
//                    val utxo: List<Any>
//                ) {
//                    data class Address(
//                        val balance: Int,
//                        val balance_usd: Int,
//                        val first_seen_receiving: Any?,
//                        val first_seen_spending: Any?,
//                        val formats: Formats,
//                        val last_seen_receiving: Any?,
//                        val last_seen_spending: Any?,
//                        val output_count: Int,
//                        val received: Int,
//                        val received_usd: Int,
//                        val script_hex: String,
//                        val scripthash_type: Any?,
//                        val spent: Int,
//                        val spent_usd: Int,
//                        val transaction_count: Int,
//                        val type: Any?,
//                        val unspent_output_count: Int
//                    ) {
//                        data class Formats(
//                            val cashaddr: String,
//                            val legacy: String
//                        )
//                    }
//                }
//            }
//        }
//
//        data class BTC(
//            val context: Context,
//            val `data`: Data
//        ) {
//            data class Context(
//                val api: Api,
//                val cache: Cache,
//                val code: Int,
//                val full_time: Double,
//                val limit: String,
//                val market_price_usd: Int,
//                val offset: String,
//                val render_time: Double,
//                val request_cost: Int,
//                val results: Int,
//                val server: String,
//                val source: String,
//                val state: Int,
//                val time: Double
//            ) {
//                data class Api(
//                    val documentation: String,
//                    val last_major_update: String,
//                    val next_major_update: Any?,
//                    val notice: String,
//                    val version: String
//                )
//
//                data class Cache(
//                    val duration: Int,
//                    val live: Boolean,
//                    val since: String,
//                    val time: Any?,
//                    val until: String
//                )
//            }
//
//            data class Data(
//                val bc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49grvtca3: Bc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49grvtca3
//            ) {
//                data class Bc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49grvtca3(
//                    val address: Address,
//                    val transactions: List<Any>,
//                    val utxo: List<Any>
//                ) {
//                    data class Address(
//                        val balance: Int,
//                        val balance_usd: Int,
//                        val first_seen_receiving: Any?,
//                        val first_seen_spending: Any?,
//                        val last_seen_receiving: Any?,
//                        val last_seen_spending: Any?,
//                        val output_count: Int,
//                        val received: Int,
//                        val received_usd: Int,
//                        val script_hex: String,
//                        val scripthash_type: Any?,
//                        val spent: Int,
//                        val spent_usd: Int,
//                        val transaction_count: Int,
//                        val type: Any?,
//                        val unspent_output_count: Int
//                    )
//                }
//            }
//        }
//
//        data class ETH(
//            val blockHash: String,
//            val blockNumber: String,
//            val confirmations: String,
//            val contractAddress: String,
//            val cumulativeGasUsed: String,
//            val from: String,
//            val gas: String,
//            val gasPrice: String,
//            val gasUsed: String,
//            val hash: String,
//            val input: String,
//            val isError: String,
//            val nonce: String,
//            val timeStamp: String,
//            val to: String,
//            val transactionIndex: String,
//            val txreceipt_status: String,
//            val value: String
//        )
//
//        data class LTC(
//            val context: Context,
//            val `data`: Data
//        ) {
//            data class Context(
//                val api: Api,
//                val cache: Cache,
//                val code: Int,
//                val full_time: Double,
//                val limit: String,
//                val market_price_usd: Double,
//                val offset: String,
//                val render_time: Double,
//                val request_cost: Int,
//                val results: Int,
//                val server: String,
//                val source: String,
//                val state: Int,
//                val time: Double
//            ) {
//                data class Api(
//                    val documentation: String,
//                    val last_major_update: String,
//                    val next_major_update: Any?,
//                    val notice: String,
//                    val version: String
//                )
//
//                data class Cache(
//                    val duration: Int,
//                    val live: Boolean,
//                    val since: String,
//                    val time: Any?,
//                    val until: String
//                )
//            }
//
//            data class Data(
//                val ltc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49g8s3u9p: Ltc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49g8s3u9p
//            ) {
//                data class Ltc1q6jzs6rzxmwzz9ddcfhlc5kfku0kuh49g8s3u9p(
//                    val address: Address,
//                    val transactions: List<Any>,
//                    val utxo: List<Any>
//                ) {
//                    data class Address(
//                        val balance: Int,
//                        val balance_usd: Int,
//                        val first_seen_receiving: Any?,
//                        val first_seen_spending: Any?,
//                        val last_seen_receiving: Any?,
//                        val last_seen_spending: Any?,
//                        val output_count: Int,
//                        val received: Int,
//                        val received_usd: Int,
//                        val script_hex: String,
//                        val scripthash_type: Any?,
//                        val spent: Int,
//                        val spent_usd: Int,
//                        val transaction_count: Int,
//                        val type: Any?,
//                        val unspent_output_count: Int
//                    )
//                }
//            }
//        }
//
//        data class NEO(
//            val entries: List<Any>,
//            val page_number: Int,
//            val page_size: Int,
//            val total_entries: Int,
//            val total_pages: Int
//        )
//
//        data class TRX(
//            val contractInfo: ContractInfo,
//            val contractMap: ContractMap,
//            val `data`: List<Data>,
//            val rangeTotal: Int,
//            val total: Int,
//            val wholeChainTxCount: Int
//        ) {
//            class ContractInfo
//
//            data class ContractMap(
//                val TE4vJCX7ctuQM6iLcsyijTDbdkwbveTwdv: Boolean,
//                val TWA2MyMVefCPpztLHPZyZohP2xKf8nj1GM: Boolean
//            )
//
//            data class Data(
//                val Events: String,
//                val SmartCalls: String,
//                val amount: String,
//                val block: Int,
//                val confirmed: Boolean,
//                val contractData: ContractData,
//                val contractRet: String,
//                val contractType: Int,
//                val cost: Cost,
//                val `data`: String,
//                val fee: String,
//                val hash: String,
//                val id: String,
//                val ownerAddress: String,
//                val result: String,
//                val revert: Boolean,
//                val timestamp: Long,
//                val toAddress: String,
//                val toAddressList: List<String>,
//                val tokenInfo: TokenInfo,
//                val tokenType: String
//            ) {
//                data class ContractData(
//                    val amount: Int,
//                    val owner_address: String,
//                    val to_address: String
//                )
//
//                data class Cost(
//                    val energy_fee: Int,
//                    val energy_usage: Int,
//                    val energy_usage_total: Int,
//                    val fee: Int,
//                    val net_fee: Int,
//                    val net_usage: Int,
//                    val origin_energy_usage: Int
//                )
//
//                data class TokenInfo(
//                    val tokenAbbr: String,
//                    val tokenCanShow: Int,
//                    val tokenDecimal: Int,
//                    val tokenId: String,
//                    val tokenLevel: String,
//                    val tokenLogo: String,
//                    val tokenName: String,
//                    val tokenType: String,
//                    val vip: Boolean
//                )
//            }
//        }
//
//        data class XLM(
//            val _embedded: Embedded,
//            val _links: Links
//        ) {
//            data class Embedded(
//                val records: List<Record>
//            ) {
//                data class Record(
//                    val _links: Links,
//                    val created_at: String,
//                    val envelope_xdr: String,
//                    val fee_account: String,
//                    val fee_charged: String,
//                    val fee_meta_xdr: String,
//                    val hash: String,
//                    val id: String,
//                    val ledger: Int,
//                    val max_fee: String,
//                    val memo_type: String,
//                    val operation_count: Int,
//                    val paging_token: String,
//                    val result_meta_xdr: String,
//                    val result_xdr: String,
//                    val signatures: List<String>,
//                    val source_account: String,
//                    val source_account_sequence: String,
//                    val successful: Boolean,
//                    val valid_after: String
//                ) {
//                    data class Links(
//                        val account: Account,
//                        val effects: Effects,
//                        val ledger: Ledger,
//                        val operations: Operations,
//                        val precedes: Precedes,
//                        val self: Self,
//                        val succeeds: Succeeds,
//                        val transaction: Transaction
//                    ) {
//                        data class Account(
//                            val href: String
//                        )
//
//                        data class Effects(
//                            val href: String,
//                            val templated: Boolean
//                        )
//
//                        data class Ledger(
//                            val href: String
//                        )
//
//                        data class Operations(
//                            val href: String,
//                            val templated: Boolean
//                        )
//
//                        data class Precedes(
//                            val href: String
//                        )
//
//                        data class Self(
//                            val href: String
//                        )
//
//                        data class Succeeds(
//                            val href: String
//                        )
//
//                        data class Transaction(
//                            val href: String
//                        )
//                    }
//                }
//            }
//
//            data class Links(
//                val next: Next,
//                val prev: Prev,
//                val self: Self
//            ) {
//                data class Next(
//                    val href: String
//                )
//
//                data class Prev(
//                    val href: String
//                )
//
//                data class Self(
//                    val href: String
//                )
//            }
//        }
//    }
//}