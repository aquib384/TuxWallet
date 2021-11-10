package io.tux.wallet.testnet.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.ADAHashReceiptModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.AdaTrxResponse
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCHashReceiptModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ERC20HashReceipt
import io.tux.wallet.testnet.model.transaction.eth.ERC20HistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ETHHashReceiptModel
import io.tux.wallet.testnet.model.transaction.eth.ETHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXHashReceiptModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xlm.XLMHashReceiptModel
import io.tux.wallet.testnet.model.transaction.xlm.XLMTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPHashReceiptModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel
import io.tux.wallet.testnet.repository.CoinTransactionRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val coinTransactionRepository: CoinTransactionRepository
) : ViewModel() {
    val errorMsg = MutableLiveData<String>()
    val successMsg = MutableLiveData<String>()


    //txnModel
    var ethTxnModel = MutableLiveData<ETHTransactionHistoryModel>()
    var ercTxnModel = MutableLiveData<ERC20HistoryModel>()
    var trxSendModel = MutableLiveData<TRXTransactionHistoryModel>()
    var xlmSendModel = MutableLiveData<XLMTransactionHistoryModel>()

    var xrpSendModel = MutableLiveData<XRPTransactionHistoryModel>()
    var btcSendModel = MutableLiveData<BTCTransactionHistoryModel>()
    var bchSendModel = MutableLiveData<BCHTransactionHistoryModel>()
    var ltcSendModel = MutableLiveData<BTCTransactionHistoryModel>()
    var adamodel = MutableLiveData<ResponseBody>()
    var adaTxmodel = MutableLiveData<AdaTrxResponse>()
    var adaTxList = MutableLiveData<ArrayList<AdaTrxResponse>>()


    //hashModel
    var ethReceiptModel = MutableLiveData<ETHHashReceiptModel>()
    var erc20ReceiptModel = MutableLiveData<ERC20HashReceipt>()
    var trxReceiptModel = MutableLiveData<TRXHashReceiptModel>()
    var xlmReceiptModel = MutableLiveData<XLMHashReceiptModel>()
    var xrpReceiptModel = MutableLiveData<XRPHashReceiptModel>()
    var btcReceiptModel = MutableLiveData<BTCHashReceiptModel>()
    var ltcReceiptModel = MutableLiveData<BTCHashReceiptModel>()
    var adaReceiptModel = MutableLiveData<ADAHashReceiptModel>()

    var sendToAdd = MutableLiveData<String>()
    var HistryType = MutableLiveData<String>()
    var selectedCoin = MutableLiveData<CoinListModel>()


    fun setScanAdd(address: String) {
        sendToAdd.postValue(address)
    }

    fun setSelectedCoin(coin: CoinListModel) {
        selectedCoin.value = coin
    }


    fun getSendADAReceipt(id: String, txnHex: String) {
        coinTransactionRepository.getAdaHashReceipt(id, txnHex).enqueue(object :
            Callback<ADAHashReceiptModel> {

            override fun onResponse(
                call: Call<ADAHashReceiptModel>,
                responseModel: Response<ADAHashReceiptModel>
            ) {


                adaReceiptModel.value = responseModel.body()


            }

            override fun onFailure(call: Call<ADAHashReceiptModel>, t: Throwable) {

                t.printStackTrace()
            }

        })

    }

    fun getSendETHReceipt(txnHex: String) {
        coinTransactionRepository.getETHHashReceipt(txnHex).enqueue(object :
            Callback<ETHHashReceiptModel> {

            override fun onResponse(
                call: Call<ETHHashReceiptModel>,
                responseModel: Response<ETHHashReceiptModel>
            ) {


                ethReceiptModel.value = responseModel.body()


            }

            override fun onFailure(call: Call<ETHHashReceiptModel>, t: Throwable) {

                t.printStackTrace()
            }

        })

    }

    fun getSendERC20Receipt(txnHex: String) {
        coinTransactionRepository.getERC20HashReceipt(txnHex).enqueue(object :
            Callback<ERC20HashReceipt> {

            override fun onResponse(
                call: Call<ERC20HashReceipt>,
                responseModel: Response<ERC20HashReceipt>
            ) {


                erc20ReceiptModel.value = responseModel.body()


            }

            override fun onFailure(call: Call<ERC20HashReceipt>, t: Throwable) {

                t.printStackTrace()
            }

        })

    }

    fun getSendTRXReceipt(txnHex: String) {
        coinTransactionRepository.getTRXHashReceipt(txnHex).enqueue(object :
            Callback<TRXHashReceiptModel> {

            override fun onResponse(
                call: Call<TRXHashReceiptModel>,
                responseModel: Response<TRXHashReceiptModel>
            ) {


                trxReceiptModel.value = responseModel.body()


            }

            override fun onFailure(call: Call<TRXHashReceiptModel>, t: Throwable) {

                t.printStackTrace()
            }

        })

    }

    fun getSendXLMReceipt(txnHex: String) {
        coinTransactionRepository.getXLMHashReceipt(txnHex)
            .enqueue(object : Callback<XLMHashReceiptModel> {

                override fun onResponse(
                    call: Call<XLMHashReceiptModel>,
                    responseModel: Response<XLMHashReceiptModel>
                ) {


                    xlmReceiptModel.value = responseModel.body()


                }

                override fun onFailure(call: Call<XLMHashReceiptModel>, t: Throwable) {

                    t.printStackTrace()
                }

            })

    }

    fun getSendXRPReceipt(txnHex: String) {
        coinTransactionRepository.getXRPHashReceipt(txnHex)
            .enqueue(object : Callback<XRPHashReceiptModel> {

                override fun onResponse(
                    call: Call<XRPHashReceiptModel>,
                    responseModel: Response<XRPHashReceiptModel>
                ) {
                    xrpReceiptModel.value = responseModel.body()
                }

                override fun onFailure(call: Call<XRPHashReceiptModel>, t: Throwable) {

                    t.printStackTrace()
                }

            })

    }

    fun getSendBTCReceipt(txnHex: String) {
        coinTransactionRepository.getBTCHashReceipt(txnHex)
            .enqueue(object : Callback<BTCHashReceiptModel> {

                override fun onResponse(
                    call: Call<BTCHashReceiptModel>,
                    responseModel: Response<BTCHashReceiptModel>
                ) {


                    btcReceiptModel.value = responseModel.body()


                }

                override fun onFailure(call: Call<BTCHashReceiptModel>, t: Throwable) {

                    t.printStackTrace()
                }

            })

    }

    fun getSendLTCReceipt(txnHex: String) {
        coinTransactionRepository.getLTCHashReceipt(txnHex)
            .enqueue(object : Callback<BTCHashReceiptModel> {

                override fun onResponse(
                    call: Call<BTCHashReceiptModel>,
                    responseModel: Response<BTCHashReceiptModel>
                ) {
                    ltcReceiptModel.value = responseModel.body()
                }

                override fun onFailure(call: Call<BTCHashReceiptModel>, t: Throwable) {

                    t.printStackTrace()
                }

            })

    }


    //Histry List

    fun getETHHistory(params: HashMap<String?, Any?>) {
        coinTransactionRepository.getETHTxnHistory(params)
            .enqueue(object : Callback<ETHTransactionHistoryModel> {
                override fun onResponse(
                    call: Call<ETHTransactionHistoryModel>,
                    response: Response<ETHTransactionHistoryModel>
                ) {
                    if (response.code() == 200) {
                        ethTxnModel.postValue(response.body())
                        successMsg.postValue(response.body()?.message.toString())
                    } else {
                        ethTxnModel.postValue(
                            ETHTransactionHistoryModel(
                                response.code(),
                                null,
                                response.errorBody()?.string()
                            )
                        )
                        errorMsg.postValue(response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<ETHTransactionHistoryModel>, t: Throwable) {
                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getERC20History(params: HashMap<String?, Any?>) {
        coinTransactionRepository.getERC20TxnHistory(params)
            .enqueue(object : Callback<ERC20HistoryModel> {

                override fun onResponse(
                    call: Call<ERC20HistoryModel>,
                    TRXTransactionHistoryModel: Response<ERC20HistoryModel>
                ) {
                    if (TRXTransactionHistoryModel.code() == 200) {
                        ercTxnModel.postValue(TRXTransactionHistoryModel.body())
                    }
                    successMsg.postValue(TRXTransactionHistoryModel.body()?.message.toString())
                }

                override fun onFailure(call: Call<ERC20HistoryModel>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }


    fun getTrxHistory(coin: CoinListModel) {
        coinTransactionRepository.getTRXTxnHistory(coin.coin_add)
            .enqueue(object : Callback<TRXTransactionHistoryModel> {

                override fun onResponse(
                    call: Call<TRXTransactionHistoryModel>,
                    response: Response<TRXTransactionHistoryModel>
                ) {
                    Log.e("trxResponseSend", response.body().toString())
                    if (response.code() == 200) {
                        trxSendModel.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<TRXTransactionHistoryModel>, t: Throwable) {
                    Log.e("TRXError", t.message.toString())
                    t.printStackTrace()
                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getXlmHistory(coin: CoinListModel) {
        coinTransactionRepository.getXLMTxnHistory(coin.coin_add)
            .enqueue(object : Callback<XLMTransactionHistoryModel> {

                override fun onResponse(
                    call: Call<XLMTransactionHistoryModel>,
                    response: Response<XLMTransactionHistoryModel>
                ) {
                    Log.e("xlm response", response.toString())
                    if (response.code() == 200) {
                        xlmSendModel.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<XLMTransactionHistoryModel>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getXrpHistory(coin: CoinListModel) {
        coinTransactionRepository.getXrpTxnHistory(coin.coin_add)
            .enqueue(object : Callback<XRPTransactionHistoryModel> {

                override fun onResponse(
                    call: Call<XRPTransactionHistoryModel>,
                    response: Response<XRPTransactionHistoryModel>
                ) {
                    Log.e("xrp response", response.body().toString())
                    if (response.code() == 200) {
                        xrpSendModel.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<XRPTransactionHistoryModel>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getBTCHistory(coin: CoinListModel) {
        coinTransactionRepository.getBTCTxnHistory(coin.coin_add)
//        coinTransactionRepository.getBTCTxnHistory("1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc")
            .enqueue(object : Callback<BTCTransactionHistoryModel> {
                override fun onResponse(
                    call: Call<BTCTransactionHistoryModel>,
                    response: Response<BTCTransactionHistoryModel>
                ) {
                    Log.e("btc response", response.body().toString())
                    if (response.code() == 200) {
                        btcSendModel.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<BTCTransactionHistoryModel>, t: Throwable) {
                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getBCHHistory(coin: CoinListModel) {
      //  coinTransactionRepository.getBCHTrxHistory(coin.coin_add)
       coinTransactionRepository.getBCHTrxHistory("qp9232aytsstvt347ls8qqnl803xgnx4a5zxxjsgel")
            .enqueue(object : Callback<BCHTransactionHistoryModel> {
                override fun onResponse(
                    call: Call<BCHTransactionHistoryModel>,
                    response: Response<BCHTransactionHistoryModel>
                ) {
                    Log.e("btc response", response.body().toString())
                    if (response.code() == 200) {
                        bchSendModel.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<BCHTransactionHistoryModel>, t: Throwable) {
                    errorMsg.postValue(t.message)
                }

            })

    }

    fun getADAHistory1(coin: CoinListModel) {
//        if (adaTxmodel.value.isNullOrEmpty()) {
        coinTransactionRepository.getADATrxHistory(coin.adaDetail.adaWalletId)
            .enqueue(object : Callback<AdaTrxResponse> {
                override fun onResponse(
                    call: Call<AdaTrxResponse>,
                    response: Response<AdaTrxResponse>
                ) {
                    Log.e("adaaaa response", response.body().toString())
                    if (response.code() == 200) {
                        adaTxmodel.postValue(response.body())
                    } else
                        adaTxmodel.postValue(AdaTrxResponse(response.code(),null,response.body()?.message))

                }

                override fun onFailure(call: Call<AdaTrxResponse>, t: Throwable) {
                    adaTxmodel.postValue(AdaTrxResponse(400,null,t.message))
                    errorMsg.postValue(t.message)
                }
            })
//        }

    }


    fun getLTCHistory(coin: CoinListModel) {
        coinTransactionRepository.getLTCTxnHistory(coin.coin_add)
            .enqueue(object : Callback<BTCTransactionHistoryModel> {

                override fun onResponse(
                    call: Call<BTCTransactionHistoryModel>,
                    response: Response<BTCTransactionHistoryModel>
                ) {
                    Log.e("ltc response", response.body().toString())
                    if (response.code() == 200) {
                        ltcSendModel.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<BTCTransactionHistoryModel>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }


}