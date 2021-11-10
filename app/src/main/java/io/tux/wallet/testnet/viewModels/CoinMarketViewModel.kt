package io.tux.wallet.testnet.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tux.wallet.testnet.model.coinResponseModel.*
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.coins.CoinModel
import io.tux.wallet.testnet.model.graph.HistoricalDataResponse
import io.tux.wallet.testnet.model.topPairResponseModel.TopPairDataItem
import io.tux.wallet.testnet.model.topPairResponseModel.TopPairResponse
import io.tux.wallet.testnet.model.wallet.AdaDetails
import io.tux.wallet.testnet.model.wallet.CoinAddressItem
import io.tux.wallet.testnet.repository.CoinMarketRepository
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.CNY
import io.tux.wallet.testnet.utils.Constants.COINS
import io.tux.wallet.testnet.utils.Constants.CURRENCY
import io.tux.wallet.testnet.utils.Constants.DISPLAY
import io.tux.wallet.testnet.utils.Constants.ERC20
import io.tux.wallet.testnet.utils.Constants.EUR
import io.tux.wallet.testnet.utils.Constants.GBP
import io.tux.wallet.testnet.utils.Constants.JPY
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_ALGO
import io.tux.wallet.testnet.utils.Constants.KEY_BTC
import io.tux.wallet.testnet.utils.Constants.KEY_ETH
import io.tux.wallet.testnet.utils.Constants.KEY_LTC
import io.tux.wallet.testnet.utils.Constants.KEY_TRX
import io.tux.wallet.testnet.utils.Constants.KEY_TUXC
import io.tux.wallet.testnet.utils.Constants.KEY_USDT
import io.tux.wallet.testnet.utils.Constants.RAW
import io.tux.wallet.testnet.utils.Constants.USD
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.utils.Utils.sortCoins
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class CoinMarketViewModel @Inject constructor(
    private val coinMarketRepository: CoinMarketRepository,
    private val sharedPref: SharedPref
) : ViewModel() {

    private var myWallet: HDWallet = if (sharedPref.getEntropy().isNullOrEmpty()) {
        HDWallet(128, "")
    } else {
        HDWallet(sharedPref.getEntropy()?.toByteArray(), "")
    }
    val errorMsg = MutableLiveData<String>()
    val successMsg = MutableLiveData<String>()

    //coinList
    var coinModel = MutableLiveData<CoinResponse>()

    var coinList = ArrayList<CoinListModel>()

    var displayList = mutableListOf<Display>()
    var rawList = mutableListOf<Raw>()
    var selectedCoin = MutableLiveData<CoinListModel>()

    //top pairs
    var topPairsList = mutableListOf<TopPairDataItem>()
    var topPairModel = MutableLiveData<TopPairResponse>()
    var rankModel = MutableLiveData<CoinResponse>()
    var jpyListModel = MutableLiveData<CoinResponse>()
    var cnyListModel = MutableLiveData<CoinResponse>()
    var eurListModel = MutableLiveData<CoinResponse>()
    var gbpListModel = MutableLiveData<CoinResponse>()

    // historical
    var dayDataModel = MutableLiveData<HistoricalDataResponse>()
    var minuteDataModel = MutableLiveData<HistoricalDataResponse>()
    var hourDataModel = MutableLiveData<HistoricalDataResponse>()

    var walletAmount = MutableLiveData<String>()

    var balanceIndex = MutableLiveData<Int>()

    val clearData = MutableLiveData<Boolean>()

    init {
        Log.i(javaClass.name, "-----------------------")
        viewModelScope.launch {
            rankListRequest(USD)
            rankListRequest(CNY)
            rankListRequest(JPY)
            rankListRequest(EUR)
            rankListRequest(GBP)
        }
    }

    private fun getCoinsBalance() {
        viewModelScope.launch {
            for (i in coinList.indices) {
                val coin = coinList[i]
                if (coin.symbol == KEY_ADA) {
                    kotlin.runCatching { coinMarketRepository.getADABalance(coin.adaDetail.adaWalletId) }
                        .onSuccess {
                            if (it.data?.balance?.total?.quantity != null) {
                                coinList[i].balance =
                                    it.data.balance.total.quantity.toDouble()
                                        .div((10.0).pow(6))
                                        .let { data ->
                                            data.let { it1 ->
                                                Utils.getBalanceData(it1).toString()
                                            }
                                        }
                                        .toString()
                                balanceIndex.value = i
                            }
                        }
                } else
                    kotlin.runCatching {
                        coinMarketRepository.getBalance(coin.coin_add, coin.symbol)
                    }.onSuccess {
                        coinList[i].balance =
                            if (it.data?.balance == null) "0.0"
                            else when (coin.symbol) {
                                KEY_BTC -> {
                                    it.data.let { data ->
                                        Utils.getBalanceData(
                                            data.balance.toString().replace(
                                                KEY_BTC, ""
                                            ).toBigDecimal()
                                        ).toString()
                                    }.toString()
                                }
                                KEY_ETH -> {
                                    it.data.let { data ->
                                        Utils.getBalanceData(
                                            data.balance.toString().toBigDecimal()
                                        ).toString()
                                    }.toString()
                                }
                                KEY_TRX -> {
                                    it.data.let { data -> Utils.getBalanceData(data) }.toString()
                                        .toDouble().div(10.0.pow(6)).toString()
                                }
                                KEY_USDT -> {
                                    it.data.let { data -> Utils.getBalanceData(data) }.toString()
                                        .toDouble().times(10.0.pow(12)).toString()
                                }
                                KEY_LTC -> {
                                    it.data.let { data ->
                                        Utils.getBalanceData(
                                            data.balance.toString().replace(KEY_LTC, " ")
                                        ).toString()
                                    }.toString()
                                }
                                KEY_ALGO -> {
                                    it.data.balance.toString().split(" ").toTypedArray()[0]
                                }
                                else -> {
                                    it.data.let { data -> Utils.getBalanceData(data).toString() }
                                        .toString()
                                }
                            }
                    }.onFailure {
                        FirebaseCrashlytics.getInstance().recordException(it)
                    }
                balanceIndex.value = i
            }
            walletAmount.value = setWalletBalance()
        }
    }

    private fun setWalletBalance(): String {
//        try {
        var walletAmount: Double? = 0.0
        coinList.forEach {
            if (it.symbol == KEY_TUXC)
            {
                walletAmount = if (sharedPref.getCurrencyCode() == "JPY") {
                    it.balance?.toDouble()?.times(834.00)
                } else {
                    it.balance?.toDouble()?.times(7.30)
                }?.let { it1 ->
                    walletAmount?.plus(
                        it1
                    )
                }
            } else {
                walletAmount = if (it.raw.currencies.isEmpty()) {
                    it.balance?.toDouble()?.times(0.0)
                } else {
                    val p: String =
                        it.raw.currencies[0].data.PRICE.toString().toBigDecimal().toString()
                    var b: Double = 0.0
                    if (it.balance != null) {
                        b = it.balance!!.toDouble()
                    }
                    b.times(p.toDouble())
                }?.let { it1 ->
                    walletAmount?.plus(
                        it1
                    )
                }
            }
        }
        return Utils.getDoubleValueFromString(walletAmount.toString())
//        }
//        catch (e:Exception)
//        {
//            e.printStackTrace()
//        }

    }

    private fun callCoinsApis() {
        val map = HashMap<String?, Any?>()
        map[COINS] = ArrayList<String>()
        map[CURRENCY] = arrayListOf(sharedPref.getCurrencyCode(),USD)
        sharedPref.getPhrase()?.let { Log.e("wallet_phrase", it) }
        viewModelScope.launch {
            coinListRequest(map)
        }
    }

    private suspend fun coinListRequest(params: HashMap<String?, Any?>) {
        kotlin.runCatching { coinMarketRepository.getCoinList(params) }.onSuccess {
            val jsonObject = JSONObject(Gson().toJson(it.data))
            coinModel.value = it
            Log.e("cryptoModel", it.toString())
            val displayListType: Type = object : TypeToken<List<Display>>() {}.type
            val rawListType: Type = object : TypeToken<List<Raw>>() {}.type
            displayList =
                Gson().fromJson(jsonObject.getString(DISPLAY), displayListType)
            rawList = Gson().fromJson(jsonObject.getString(RAW), rawListType)

//                   Log.e("displayList",displayList.toString())
//                   Log.e("rawList",rawList.toString())
            for (i in 0 until coinList.size) {
                val coin = coinList[i]
                for (j in 0 until displayList.size) {
                    if (coin.symbol == displayList[j].coin && coin.symbol == rawList[j].coin) {
                        coin.display = displayList[j]
                        coin.raw = rawList[j]
                    }

                }
            }
            kotlin.runCatching {
                coinList.sortCoins()
            }.onFailure {
                Log.i("TAG", "coinListRequest: ${it.message}")
            }


            getCoinsBalance()

//                   Log.e("coinsListViewModelDISPLAY",coinList[0].display.toString())
//                   Log.e("coinsListViewModelRAW",coinList[0].raw.toString())
            Log.e("coinList", coinList.toString())
        }
    }

    private suspend fun rankListRequest(currency: String) {
        val map = HashMap<String?, Any?>()
        map[COINS] = ArrayList<String>()
        map[CURRENCY] = arrayListOf(currency)
        kotlin.runCatching { coinMarketRepository.getCoinList(map) }.onSuccess {
            when (currency) {
                GBP -> gbpListModel.postValue(it)
                CNY -> cnyListModel.postValue(it)
                JPY -> jpyListModel.postValue(it)
                EUR -> eurListModel.postValue(it)
                else -> rankModel.postValue(it)
            }
        }.onFailure {
            errorMsg.postValue(it.message)
        }


    }


    fun coinDayData(params: HashMap<String?, Any?>) {
        coinMarketRepository.getCoinDayData(params)
            .enqueue(object : Callback<HistoricalDataResponse> {

                override fun onResponse(
                    call: Call<HistoricalDataResponse>,
                    responseModel: Response<HistoricalDataResponse>
                ) {
                    if (responseModel.code() == 200 && responseModel.body()?.code == 200) {
                        dayDataModel.value = responseModel.body()!!

                    }
                    successMsg.postValue(responseModel.body()?.message.toString())
                }

                override fun onFailure(call: Call<HistoricalDataResponse>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }

    fun coinHourData(params: HashMap<String?, Any?>) {
        coinMarketRepository.getCoinHourData(params)
            .enqueue(object : Callback<HistoricalDataResponse> {

                override fun onResponse(
                    call: Call<HistoricalDataResponse>,
                    responseModel: Response<HistoricalDataResponse>
                ) {
                    if (responseModel.code() == 200 && responseModel.body()?.code == 200) {
                        hourDataModel.value = responseModel.body()!!

                    }
                    successMsg.postValue(responseModel.body()?.message.toString())
                }

                override fun onFailure(call: Call<HistoricalDataResponse>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }

            })

    }

    fun coinMinuteData(params: HashMap<String?, Any?>) {
        coinMarketRepository.getCoinMinData(params)
            .enqueue(object : Callback<HistoricalDataResponse> {
                override fun onResponse(
                    call: Call<HistoricalDataResponse>,
                    responseModel: Response<HistoricalDataResponse>
                ) {
                    if (responseModel.code() == 200 && responseModel.body()?.code == 200) {
                        minuteDataModel.value = responseModel.body()!!

                    }
                    successMsg.postValue(responseModel.body()?.message.toString())
                }

                override fun onFailure(call: Call<HistoricalDataResponse>, t: Throwable) {

                    errorMsg.postValue(t.message)
                }
            })
    }


    fun getErcCoins() {
        coinMarketRepository.getERC20Coins().enqueue(
            object : Callback<CoinModel> {
                override fun onResponse(call: Call<CoinModel>, response: Response<CoinModel>) {
                    if (response.body()?.code == 200) {
                        val list = response.body()!!.data as ArrayList<CoinModel.CoinItem>
                        Log.e("ercList", list.toString())
                        coinList.clear()
                        list.forEach {
                            if (it.status == Constants.ACTIVE) {
                                setCoin(it)
//                              }
                            }
                        }
                    }
                    setSetUpCoins()
                }

                override fun onFailure(call: Call<CoinModel>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("error", t.localizedMessage)
                }

            }
        )
    }


    fun setSetUpCoins() {
        val xrpKey = myWallet.getKeyForCoin(CoinType.XRP)
        val publicKeyXrp = xrpKey.getPublicKeySecp256k1(true)

        val xlmKey = myWallet.getKeyForCoin(CoinType.STELLAR)
        val publicKeyXLm = xrpKey.getPublicKeySecp256k1(true)

        coinList.forEach {
            when (it.symbol) {
                KEY_ETH -> {
                    if (sharedPref.isLogin()) {
                        it.coin_add = sharedPref.getEthAddress().toString()
                        it.coin_key = sharedPref.getEthKey().toString()
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.ETHEREUM)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.ETHEREUM).data(),
                                false
                            )
                    }
                }
                Constants.KEY_TRX -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.TRON)
                    it.coin_key =
                        Utils.getHexString(myWallet.getKeyForCoin(CoinType.TRON).data(), false)
                }
                Constants.KEY_DOT -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.POLKADOT)
                    it.coin_key =
                        Utils.getHexString(
                            myWallet.getKeyForCoin(CoinType.POLKADOT).data(),
                            false
                        )
                }
                KEY_BTC -> {
                    if (sharedPref.isLogin()) {
                        it.coin_add = sharedPref.getBtcAddress().toString()
                        it.coin_key = sharedPref.getWIF().toString()
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.BITCOIN)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.BITCOIN).data(),
                                false
                            )
                    }
                }
                Constants.KEY_BCH -> {
                    if (sharedPref.isLogin()) {
                        it.coin_add = sharedPref.getBchAddress().toString()
                        it.coin_key = sharedPref.getBCHWIF().toString()
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.BITCOINCASH)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.BITCOINCASH).data(),
                                false
                            )
                    }
                }
                Constants.KEY_XRP -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.XRP)
                    it.secret_key = publicKeyXrp.description()
                    it.coin_key =
                        Utils.getHexString(myWallet.getKeyForCoin(CoinType.XRP).data(), false)
                }
                Constants.KEY_NEO -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.NEO)
                    it.coin_key =
                        Utils.getHexString(myWallet.getKeyForCoin(CoinType.NEO).data(), false)
                }
                Constants.KEY_XLM -> {
                    if (sharedPref.isLogin()) {
                        var savedCoin = Utils.getSavedAddress(it, sharedPref)
                        it.coin_add = savedCoin.coin_add
                        it.coin_key = savedCoin.coin_key
                        it.secret_key = savedCoin.secret_key
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.STELLAR)
                        it.secret_key = publicKeyXLm.description()
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.STELLAR).data(),
                                false
                            )
                    }

                }
                Constants.KEY_EOS -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.EOS)
                    it.coin_key =
                        Utils.getHexString(myWallet.getKeyForCoin(CoinType.EOS).data(), false)
                }
                Constants.KEY_ATOM -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.COSMOS)
                    it.coin_key =
                        Utils.getHexString(
                            myWallet.getKeyForCoin(CoinType.COSMOS).data(),
                            false
                        )
                }
                Constants.KEY_ALGO -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.ALGORAND)
                    it.coin_key =
                        Utils.getHexString(
                            myWallet.getKeyForCoin(CoinType.ALGORAND).data(),
                            false
                        )
                }
                KEY_LTC -> {
                    if (sharedPref.isLogin()) {
                        it.coin_add = sharedPref.getLtcAddress().toString()
                        it.coin_key = sharedPref.getLTCWIF().toString()
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.LITECOIN)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.LITECOIN).data(),
                                false
                            )
                    }
                }
                Constants.KEY_XTZ -> {
                    it.coin_add = myWallet.getAddressForCoin(CoinType.TEZOS)
                    it.coin_key =
                        Utils.getHexString(myWallet.getKeyForCoin(CoinType.TEZOS).data(), false)
                }
                Constants.KEY_ADA -> {
                    if (sharedPref.isLogin()) {
                        var savedCoin = Utils.getSavedAddress(it, sharedPref)
                        it.adaDetail = savedCoin.adaDetail
                        it.coin_add = savedCoin.coin_add
                        it.coin_key = savedCoin.coin_key
                        it.secret_key = savedCoin.secret_key
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.CARDANO)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.CARDANO).data(),
                                false
                            )
                    }
                }
                else -> {
                    if (sharedPref.isLogin()) {
                        it.coin_add = sharedPref.getEthAddress().toString()
                        it.coin_key = sharedPref.getEthKey().toString()
                    } else {
                        it.coin_add = myWallet.getAddressForCoin(CoinType.ETHEREUM)
                        it.coin_key =
                            Utils.getHexString(
                                myWallet.getKeyForCoin(CoinType.ETHEREUM).data(),
                                false
                            )
                    }
                }
            }
        }
//        } else {

//        }
        callCoinsApis()

    }

    fun setCoin(it: CoinModel.CoinItem) {
        var isToken = it.coinType == ERC20
        var img = if (it.coinType == ERC20)
            it.icon
        else
            ""
        var contractId = if (it.coinType == ERC20)
            it.contractAddress
        else
            ""
        val adaDetails =
            AdaDetails(ArrayList<CoinAddressItem.AdaCoinAddressDetail>(), "", "", "")

        val display = listOf(
            DisplayCurrency(
                sharedPref.getCurrencyCode()!!,
                CoinDataItem(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            )
        )
        val raw = listOf(
            RawCurrency(
                sharedPref.getCurrencyCode()!!,
                CoinDataItem(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            )
        )
        coinList.add(
            CoinListModel(
                it.id,
                it.fullName,
                it.shortName,
                "",
                "",
                "",
                contractId.toString(),
                isToken,
                "0",
                img,
                Display("", "", "", display, ""),
                Raw("", raw),
                adaDetails, false
            )
        )
    }
}