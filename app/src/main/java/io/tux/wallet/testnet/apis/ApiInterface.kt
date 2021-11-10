package io.tux.wallet.testnet.apis


import io.tux.wallet.testnet.model.*
import io.tux.wallet.testnet.model.address.ADAAddressModel
import io.tux.wallet.testnet.model.address.LTCAddressModel
import io.tux.wallet.testnet.model.address.XLMAddressModel
import io.tux.wallet.testnet.model.address.XRPAddressModel
import io.tux.wallet.testnet.model.coinResponseModel.CoinResponse
import io.tux.wallet.testnet.model.coins.BalanceResponseModel
import io.tux.wallet.testnet.model.coins.CoinModel
import io.tux.wallet.testnet.model.exchangeResponse.ExchangeResponseModel
import io.tux.wallet.testnet.model.graph.HistoricalDataResponse
import io.tux.wallet.testnet.model.stake.StakeResponse
import io.tux.wallet.testnet.model.stake.StakesHistoryModel
import io.tux.wallet.testnet.model.stake.SubmitStakeModel
import io.tux.wallet.testnet.model.transaction.ada.ADAHashReceiptModel
import io.tux.wallet.testnet.model.transaction.ada.AdaBalanceModel
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
import io.tux.wallet.testnet.model.wallet.MapAddressModel
import io.tux.wallet.testnet.model.wallet.RecoverWalletResponseModel
import io.tux.wallet.testnet.utils.Urls.ADA_STAKE_URL
import io.tux.wallet.testnet.utils.Urls.ADD_TX
import io.tux.wallet.testnet.utils.Urls.COINS_LIST_URL
import io.tux.wallet.testnet.utils.Urls.COINS_URL
import io.tux.wallet.testnet.utils.Urls.COIN_BALANCE
import io.tux.wallet.testnet.utils.Urls.COIN_DAY_HISTORICAL_URL
import io.tux.wallet.testnet.utils.Urls.COIN_HOUR_HISTORICAL_URL
import io.tux.wallet.testnet.utils.Urls.COIN_MIN_HISTORICAL_URL
import io.tux.wallet.testnet.utils.Urls.CREATE_ENTROPY
import io.tux.wallet.testnet.utils.Urls.CREATE_WALLET_URL
import io.tux.wallet.testnet.utils.Urls.DETECT_LANGUAGE
import io.tux.wallet.testnet.utils.Urls.FCM_SAVE
import io.tux.wallet.testnet.utils.Urls.FCM_UPDATE_STATUS
import io.tux.wallet.testnet.utils.Urls.GEN_ADA_URL
import io.tux.wallet.testnet.utils.Urls.GEN_BTC_URL
import io.tux.wallet.testnet.utils.Urls.GEN_LTC_URL
import io.tux.wallet.testnet.utils.Urls.GEN_RIPPLE_URL
import io.tux.wallet.testnet.utils.Urls.GEN_STELLAR_URL
import io.tux.wallet.testnet.utils.Urls.GET_ALGO_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_ALL_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_ATOM_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_BCH_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_BCH_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_BTC_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_BTC_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_BTC_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_EOS_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_ERC20_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_ERC20_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_ETH_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_ETH_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_ETH_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_FCM_STATUS
import io.tux.wallet.testnet.utils.Urls.GET_LTC_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_LTC_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_LTC_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_NEM_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_NEO_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_TRX_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_TRX_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_TRX_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_XLM_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_XLM_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_XLM_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_XRP_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.GET_XRP_TXN_DETAILS_URL
import io.tux.wallet.testnet.utils.Urls.GET_XRP_TXN_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.GET_XTZ_BALANCE_URL
import io.tux.wallet.testnet.utils.Urls.MAP_WALLET_ADDRESSES_URL
import io.tux.wallet.testnet.utils.Urls.MARKET_EXCHANGE_PAIRS_URL
import io.tux.wallet.testnet.utils.Urls.RECOVER_WALLET_URL
import io.tux.wallet.testnet.utils.Urls.SEND_EMAIL
import io.tux.wallet.testnet.utils.Urls.STAKE_ADD_URL
import io.tux.wallet.testnet.utils.Urls.STAKE_HISTORY_URL
import io.tux.wallet.testnet.utils.Urls.UPDATE_BIOMETRIC_URL
import io.tux.wallet.testnet.utils.Urls.UPDATE_WALLET_PIN_URL
import io.tux.wallet.testnet.utils.Urls.XTZ_STAKE_URL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST(CREATE_WALLET_URL)
    fun createNewWallet(@Body params: HashMap<String?, Any?>): Call<ResponseBody>

    @POST(RECOVER_WALLET_URL)
    fun recoverWallet(@Body params: HashMap<String?, Any?>): Call<ResponseBody>

    @GET(COINS_URL)
    fun getCoins(): Call<CoinModel>

    @GET(DETECT_LANGUAGE)
    fun detectLang(@Query("text") text: String): Call<LanguageDetectModel>

    @PUT(UPDATE_WALLET_PIN_URL)
    fun updateWalletPin(
        @Query("walletPin") pin: String,
        @Query("walletId") walletId: Long
    ): Call<RecoverWalletResponseModel>

    @PUT(UPDATE_BIOMETRIC_URL)
    fun updateBiometrics(
        @Query("lockType") lockType: String,
        @Query("walletId") walletId: Long
    ): Call<RecoverWalletResponseModel>

    @PUT(ADD_TX)
    fun updateTxCount(@Body map: HashMap<String, Any>): Call<UpdateTxCountModel>


    @POST(MAP_WALLET_ADDRESSES_URL)
    fun linkAddressToWalletId2(@Body list: ArrayList<HashMap<String, Any>>): Call<MapAddressModel>

    @POST(COINS_LIST_URL)
    suspend fun getCoinsListData(@Body params: HashMap<String?, Any?>): CoinResponse

    @POST(SEND_EMAIL)
    fun submitEmail(@Body params: HashMap<String?, Any?>): Call<ContactUsModel>

    @POST(FCM_SAVE)
    fun setFCMStatus(@Body params: HashMap<String?, Any?>): Call<SaveFcmStatusResponse>

    @PUT(FCM_UPDATE_STATUS)
    fun updateFCMStatus(@Body params: HashMap<String?, Any?>): Call<SaveFcmStatusResponse>

    @GET(GET_FCM_STATUS)
    fun getFCMStatus(@Query("userDetailsId") id: String): Call<FCMStatusModel>

    @GET(MARKET_EXCHANGE_PAIRS_URL)
    fun getMarketPairs(
        @Query("coin") coin: String,
        @Query("limit") limit: Int
    ): Call<ExchangeResponseModel>

    @POST(COIN_DAY_HISTORICAL_URL)
    fun getHistoricalDataDay(@Body params: HashMap<String?, Any?>): Call<HistoricalDataResponse>

    @POST(COIN_MIN_HISTORICAL_URL)
    fun getHistoricalDataMinute(@Body params: HashMap<String?, Any?>): Call<HistoricalDataResponse>

    @POST(COIN_HOUR_HISTORICAL_URL)
    fun getHistoricalDataHour(@Body params: HashMap<String?, Any?>): Call<HistoricalDataResponse>

    // txnHistory
    @POST(GET_ETH_TXN_HISTORY_URL)
    fun getETHTxnHistory(@Body params: HashMap<String?, Any?>): Call<ETHTransactionHistoryModel>

    @POST(GET_ERC20_TXN_HISTORY_URL)
    fun getERC20TxnHistory(@Body params: HashMap<String?, Any?>): Call<ERC20HistoryModel>

    @GET(GET_TRX_TXN_HISTORY_URL)
    fun getTRXTxnHistory(@Query("address") address: String): Call<TRXTransactionHistoryModel>

    @GET(GET_XLM_TXN_HISTORY_URL)
    fun getXLMTxnHistory(@Query("address") address: String): Call<XLMTransactionHistoryModel>

    @GET(GET_XRP_TXN_HISTORY_URL)
    fun getXRPTransactionHistory(@Query("address") address: String): Call<XRPTransactionHistoryModel>

    @GET(GET_BTC_TXN_HISTORY_URL)
    fun getBTCTransactionHistory(@Query("address") address: String): Call<BTCTransactionHistoryModel>

    @GET(GET_LTC_TXN_HISTORY_URL)
    fun getLTCTransactionHistory(@Query("address") address: String): Call<BTCTransactionHistoryModel>

    @GET(GET_BCH_TXN_HISTORY_URL)
    fun getBCHTrxHistory(@Query("address") address: String): Call<BCHTransactionHistoryModel>


    //    @GET("https://testnet-user.tux-wallet.io/api/v1/ada/wallets/{id}/transactions")
    @GET("https://user.tux-wallet.com/api/v1/ada/wallet/{id}/transactions")
    fun getADATrxHistory(@Path("id") id: String): Call<AdaTrxResponse>



    @POST(GET_ALL_TXN_HISTORY_URL)
    fun getAllTxnHistory(@Body params: HashMap<String?, Any?>): Call<ResponseBody>

    //balance apis
    @GET(GET_ETH_BALANCE_URL)
    suspend fun getETHBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_BTC_BALANCE_URL)
    suspend fun getBTCBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_BCH_BALANCE_URL)
    suspend fun getBCHBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_XRP_BALANCE_URL)
    suspend fun getXRPBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_NEO_BALANCE_URL)
    suspend fun getNEOBalance(@Query("address") address: String): BalanceResponseModel

    @POST(GET_ERC20_BALANCE_URL)
    suspend fun getERC20Balance(@Body params: HashMap<String?, Any?>): BalanceResponseModel

    @GET(GET_ALGO_BALANCE_URL)
    suspend fun getALGOBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_ATOM_BALANCE_URL)
    suspend fun getATomBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_EOS_BALANCE_URL)
    suspend fun getEOSBalance(@Query("account") address: String): BalanceResponseModel

    @GET(GET_LTC_BALANCE_URL)
    suspend fun getLTCBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_XTZ_BALANCE_URL)
    suspend fun getXTZBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_TRX_BALANCE_URL)
    suspend fun getTRXBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_NEM_BALANCE_URL)
    suspend fun getNEMBalance(@Query("address") address: String): BalanceResponseModel

    @GET(GET_XLM_BALANCE_URL)
    suspend fun getXLMBalance(@Query("address") address: String): BalanceResponseModel

    @GET(COIN_BALANCE)
    suspend fun getBalance(
        @Query("address") address: String,
        @Query("coin") coin: String
    ): BalanceResponseModel


    //    @GET("https://testnet-user.tux-wallet.io/api/v1/ada/balance/details/{id}")
    @GET("https://user.tux-wallet.com/api/v1/ada/balance/details/{id}")
    suspend fun getADABalance(@Path("id") id: String): AdaBalanceModel

    //    @GET("https://testnet-user.tux-wallet.io/api/v1/ada/wallets/{id}/transactions/{tid}")
    @GET("https://user.tux-wallet.com/api/v1/ada/wallets/{id}/transactions/{tid}")
    fun getADAHashReceipt(
        @Path("id") id: String,
        @Path("tid") tid: String
    ): Call<ADAHashReceiptModel>

    //
//    @POST("https://testnet-user.tux-wallet.io/api/v1/{coin}/send")
    @POST("https://user.tux-wallet.com/api/v1/{coin}/send")
    fun sendCoins(
        @Path("coin") coin: String,
        @Body params: @JvmSuppressWildcards HashMap<String?, Any?>
    ): Call<ResponseBody>


    //stakes
    @POST(ADA_STAKE_URL)
    fun stakeAda(@Body params: @JvmSuppressWildcards Map<String, Any?>): Call<StakeResponse>

    @POST(XTZ_STAKE_URL)
    fun stakeXTZ(@Body params: @JvmSuppressWildcards Map<String, Any?>): Call<StakeResponse>

    @POST(STAKE_ADD_URL)
    fun submitStake(@Body params: @JvmSuppressWildcards Map<String, Any?>): Call<SubmitStakeModel>

    @GET(STAKE_HISTORY_URL)
    fun getStakesList(@Query("walletDetailsId") id: String): Call<StakesHistoryModel>

    //generate address
    @GET(GEN_RIPPLE_URL)
    fun getRippleAddress(): Call<XRPAddressModel>

    @POST(GEN_STELLAR_URL)
    fun getStellerAddress(@Body params: @JvmSuppressWildcards Map<String, Any?>): Call<XLMAddressModel>

    @POST(GEN_ADA_URL)
    fun getADAAddress(@Body params: @JvmSuppressWildcards Map<String, Any?>): Call<ADAAddressModel>

    @GET(GEN_BTC_URL)
    fun getBTCAddress(): Call<LTCAddressModel>

    @GET(GEN_LTC_URL)
    fun getLTCAddress(): Call<LTCAddressModel>


    // trxHistryDetails
    @GET(GET_ETH_TXN_DETAILS_URL)
    fun getETHHashReceipt(@Query("txhash") txnHex: String): Call<ETHHashReceiptModel>

    @GET(GET_ETH_TXN_DETAILS_URL)
    fun getERCHashReceipt(@Query("txhash") txnHex: String): Call<ERC20HashReceipt>

    @GET(GET_TRX_TXN_DETAILS_URL)
    fun getTRXHashReceipt(@Query("txhash") txnHex: String): Call<TRXHashReceiptModel>

    @GET(GET_XLM_TXN_DETAILS_URL)
    fun getXLMHashReceipt(@Query("txhash") txnHex: String): Call<XLMHashReceiptModel>

    @GET(GET_XRP_TXN_DETAILS_URL)
    fun getXRPHashReceipt(@Query("txhash") txnHex: String): Call<XRPHashReceiptModel>

    @GET(GET_BTC_TXN_DETAILS_URL)
    fun getBTCHashReceipt(@Query("txhash") txnHex: String): Call<BTCHashReceiptModel>

    @GET(GET_LTC_TXN_DETAILS_URL)
    fun getLTCHashReceipt(@Query("txhash") txnHex: String): Call<BTCHashReceiptModel>


    @GET(CREATE_ENTROPY)
    suspend fun getEntropy(
        @QueryMap params: @JvmSuppressWildcards Map<String, Any?>
    ): ResponseBody


}