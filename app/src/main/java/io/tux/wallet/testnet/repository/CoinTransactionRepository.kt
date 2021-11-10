package io.tux.wallet.testnet.repository

import io.tux.wallet.testnet.apis.ApiInterface
import javax.inject.Inject

class CoinTransactionRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getETHTxnHistory(params: HashMap<String?, Any?>) = apiInterface.getETHTxnHistory(params)
    fun getERC20TxnHistory(params: HashMap<String?, Any?>) = apiInterface.getERC20TxnHistory(params)
    fun getTRXTxnHistory(add: String) = apiInterface.getTRXTxnHistory(add)
    fun getXLMTxnHistory(add: String) = apiInterface.getXLMTxnHistory(add)
    fun getXrpTxnHistory(add: String) = apiInterface.getXRPTransactionHistory(add)
    fun getBTCTxnHistory(add: String) = apiInterface.getBTCTransactionHistory(add)
    fun getLTCTxnHistory(add: String) = apiInterface.getLTCTransactionHistory(add)
    fun getADATrxHistory(add: String) = apiInterface.getADATrxHistory(add)
    fun getBCHTrxHistory(add: String) = apiInterface.getBCHTrxHistory(add)

    // hash receipt
    fun getETHHashReceipt(txnHex: String) = apiInterface.getETHHashReceipt(txnHex)
    fun getERC20HashReceipt(txnHex: String) = apiInterface.getERCHashReceipt(txnHex)
    fun getTRXHashReceipt(txnHex: String) = apiInterface.getTRXHashReceipt(txnHex)
    fun getXLMHashReceipt(txnHex: String) = apiInterface.getXLMHashReceipt(txnHex)
    fun getXRPHashReceipt(txnHex: String) = apiInterface.getXRPHashReceipt(txnHex)
    fun getBTCHashReceipt(txnHex: String) = apiInterface.getBTCHashReceipt(txnHex)
    fun getLTCHashReceipt(txnHex: String) = apiInterface.getLTCHashReceipt(txnHex)
    fun getAdaHashReceipt(id: String, txId: String) = apiInterface.getADAHashReceipt(id, txId)
}