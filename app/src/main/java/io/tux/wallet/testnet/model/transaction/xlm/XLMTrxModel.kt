package io.tux.wallet.testnet.model.transaction.xlm

import com.google.gson.annotations.SerializedName

data class XLMTrxModel (

    @SerializedName("code") val code : Int,
    @SerializedName("data") val data : Data,
    @SerializedName("message") val message : String
)
data class Data (

    @SerializedName("_links") val _links : _links,
    @SerializedName("_embedded") val _embedded : _embedded
)
data class _embedded (

    @SerializedName("records") val records : List<Records>
)
data class _links (

    @SerializedName("self") val self : Self,
    @SerializedName("account") val account : Account,
    @SerializedName("ledger") val ledger : Ledger,
    @SerializedName("operations") val operations : Operations,
    @SerializedName("effects") val effects : Effects,
    @SerializedName("precedes") val precedes : Precedes,
    @SerializedName("succeeds") val succeeds : Succeeds,
    @SerializedName("transaction") val transaction : Transaction
)
data class Succeeds (

    @SerializedName("href") val href : String
)
data class Account (

    @SerializedName("href") val href : String
)
data class Next (

    @SerializedName("href") val href : String
)
data class Precedes (

    @SerializedName("href") val href : String
)
data class Effects (

    @SerializedName("href") val href : String,
    @SerializedName("templated") val templated : Boolean
)

data class Ledger (

    @SerializedName("href") val href : String
)
data class Operations (

    @SerializedName("href") val href : String,
    @SerializedName("templated") val templated : Boolean
)
data class Prev (

    @SerializedName("href") val href : String
)
data class Self (

    @SerializedName("href") val href : String
)
data class Transaction (

    @SerializedName("href") val href : String
)
data class Records (

    @SerializedName("_links") val _links : _links,
    @SerializedName("id") val id : String,
    @SerializedName("paging_token") val paging_token : Int,
    @SerializedName("successful") val successful : Boolean,
    @SerializedName("hash") val hash : String,
    @SerializedName("ledger") val ledger : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("source_account") val source_account : String,
    @SerializedName("source_account_sequence") val source_account_sequence : Int,
    @SerializedName("fee_account") val fee_account : String,
    @SerializedName("fee_charged") val fee_charged : Int,
    @SerializedName("max_fee") val max_fee : Int,
    @SerializedName("operation_count") val operation_count : Int,
    @SerializedName("envelope_xdr") val envelope_xdr : String,
    @SerializedName("result_xdr") val result_xdr : String,
    @SerializedName("result_meta_xdr") val result_meta_xdr : String,
    @SerializedName("fee_meta_xdr") val fee_meta_xdr : String,
    @SerializedName("memo_type") val memo_type : String,
    @SerializedName("signatures") val signatures : List<String>,
    @SerializedName("valid_after") val valid_after : String
)