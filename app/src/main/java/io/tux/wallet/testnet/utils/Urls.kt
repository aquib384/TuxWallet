package io.tux.wallet.testnet.utils

object Urls {
    //    const val BASE_URL2 = "http://18.142.36.187:7006/"
    const val BASE_URL = "https://user.tux-wallet.com/"
    private const val BASE_URL2 = "https://testnet-user.tux-wallet.io/"
    private const val BASE_URL_CRYPTO_COMPARE = "cryptocurrency/"
    private const val ROOT_URL = BASE_URL + "api/v1/"
    private const val ROOT_URL_V2 = BASE_URL + "api/v2/"
    private const val ROOT_URL2 = BASE_URL + "api/v2/"

    // wallet
    const val CREATE_WALLET_URL = ROOT_URL + "create_wallet"
    const val RECOVER_WALLET_URL = ROOT_URL + "wallet/recover"
    const val UPDATE_WALLET_PIN_URL = ROOT_URL + "update/wallet/pin"
    const val UPDATE_BIOMETRIC_URL = ROOT_URL + "update/wallet/biometrics"
    const val MAP_WALLET_ADDRESSES_URL = ROOT_URL + "address/save"
    const val STAKE_HISTORY_URL = ROOT_URL + "wallet/stake/list"
    const val STAKE_ADD_URL = ROOT_URL + "wallet/stake/save"
    const val CREATE_ENTROPY = ROOT_URL + "entropy/generate"
    const val ADD_TX = ROOT_URL + "tx/stats"
    const val COIN_BALANCE = ROOT_URL_V2 + "balance/details"
    const val DETECT_LANGUAGE = ROOT_URL + "language/dedection"

    //coinlist
    const val COINS_LIST_URL = ROOT_URL + BASE_URL_CRYPTO_COMPARE + "latest/listing"
    const val COINS_URL = ROOT_URL + "coins"


    // graph
    const val COIN_MIN_HISTORICAL_URL = ROOT_URL + "coin/historical/details/minute"
    const val COIN_HOUR_HISTORICAL_URL = ROOT_URL + "coin/historical/details/hourly"
    const val COIN_DAY_HISTORICAL_URL = ROOT_URL + "coin/historical/details/day"

    // market
    const val MARKET_EXCHANGE_PAIRS_URL = ROOT_URL + "top/exchange/pairs/details"

    //reciept
    const val GET_ETH_TXN_DETAILS_URL = ROOT_URL + "eth/transaction/receipt"
    const val GET_TRX_TXN_DETAILS_URL = ROOT_URL + "tron/hash/status"
    const val GET_XLM_TXN_DETAILS_URL = ROOT_URL + "xlm/hash/status"
    const val GET_XRP_TXN_DETAILS_URL = ROOT_URL + "xrp/hash/status"
    const val GET_BTC_TXN_DETAILS_URL = ROOT_URL + "btc/hash/status"
    const val GET_LTC_TXN_DETAILS_URL = ROOT_URL + "ltc/hash/status"
    const val GET_ADA_TXN_DETAILS_URL = ROOT_URL + "ada/hash/status"
//    const val GET_ADA_TXN_DETAILS_URL = ROOT_URL + "http://18.142.143.70:8090/v2/wallets/{walletId}/transactions/{transactionId}"

    //coin balance
    const val GET_ETH_BALANCE_URL = ROOT_URL + "eth/balance"
    const val GET_ALGO_BALANCE_URL = ROOT_URL + "algo/balance/details"
    const val GET_ATOM_BALANCE_URL = ROOT_URL + "atom/balance/details"
    const val GET_BCH_BALANCE_URL = ROOT_URL + "bch/balance/details"
    const val GET_BTC_BALANCE_URL = ROOT_URL + "btc/balance/details"
    const val GET_EOS_BALANCE_URL = ROOT_URL + "eos/balance"
    const val GET_ERC20_BALANCE_URL = ROOT_URL + "erc20/balance"
    const val GET_LTC_BALANCE_URL = ROOT_URL + "ltc/balance/details"
    const val GET_NEM_BALANCE_URL = ROOT_URL + "nem/balance/details"
    const val GET_NEO_BALANCE_URL = ROOT_URL + "neo/balance/details"
    const val GET_TRX_BALANCE_URL = ROOT_URL + "trx/balance/details"
    const val GET_XRP_BALANCE_URL = ROOT_URL + "xrp/balance/details"
    const val GET_XTZ_BALANCE_URL = ROOT_URL + "xtz/balance/details"
    const val GET_XLM_BALANCE_URL = ROOT_URL + "xlm/balance"

    // coin transaction
    const val GET_ETH_TXN_HISTORY_URL = ROOT_URL + "eth/tx/history"
    const val GET_ALGO_TXN_HISTORY_URL = ROOT_URL + "algo/tx/details"
    const val GET_ATOM_TXN_HISTORY_URL = ROOT_URL + "atom/tx/details"
    const val GET_BCH_TXN_HISTORY_URL = ROOT_URL + "bch/tx/details"
    const val GET_BTC_TXN_HISTORY_URL = ROOT_URL + "btc/tx/details"
    const val GET_EOS_TXN_HISTORY_URL = ROOT_URL + "eos/tx/history"
    const val GET_ERC20_TXN_HISTORY_URL = ROOT_URL + "erc20/tx/history"
    const val GET_LTC_TXN_HISTORY_URL = ROOT_URL + "ltc/tx/details"
    const val GET_NEM_TXN_HISTORY_URL = ROOT_URL + "nem/tx/details"
    const val GET_NEO_TXN_HISTORY_URL = ROOT_URL + "neo/tx/details"
    const val GET_TRX_TXN_HISTORY_URL = ROOT_URL + "trx/tx/details"
    const val GET_XRP_TXN_HISTORY_URL = ROOT_URL + "xrp/tx/details"
    const val GET_XTZ_TXN_HISTORY_URL = ROOT_URL + "xtz/tx/details"
    const val GET_XLM_TXN_HISTORY_URL = ROOT_URL + "xlm/tx/history"
    const val GET_ALL_TXN_HISTORY_URL = ROOT_URL + "tx/history"

//    const val SEND_STELLAR_URL = ROOT_URL + "steller/send"
//    const val SEND_ERC20_URL = ROOT_URL + "erc20/send"
//    const val SEND_RIPPLE_URL = ROOT_URL + "ripple/send"
//    const val SEND_BTC_URL = ROOT_URL + "btc/send"
//    const val SEND_LTC_URL = ROOT_URL + "ltc/send"
//    const val SEND_ADA_URL = ROOT_URL + "ada/send"
//    const val SEND_TRX_URL = ROOT_URL + "tron/send"
//    const val SEND_BCH_URL = ROOT_URL + "bch/send"

    // stake urls
    const val ADA_STAKE_URL = ROOT_URL + "ada/staking"
    const val XTZ_STAKE_URL = ROOT_URL + "tron/staking"


    //gen address urls
    const val GEN_ADA_URL = ROOT_URL + "ada/address"
    const val GEN_BTC_URL = ROOT_URL + "btc/address"
    const val GEN_LTC_URL = ROOT_URL + "ltc/address"
    const val GEN_RIPPLE_URL = ROOT_URL + "ripple/address"
    const val GEN_STELLAR_URL = ROOT_URL + "steller/address"


    // redirect urls
    //* Mainnet
    const val LTC_M_EXPLORER_URL = "https://live.blockcypher.com/ltc/tx/"
    const val ADA_M_EXPLORER_URL = "https://explorer.cardano.org/en/transaction?id="

    //* Testnet

/*    const val BTC_T_EXPLORER_URL = "https://live.blockcypher.com/btc-testnet/tx/"
//    const val BTC_M_EXPLORER_URL = "https://live.blockcypher.com/btc/tx/"
    const val BTC_M_EXPLORER_URL = "https://www.blockchain.com/btc/tx/"
    const val ETH_T_EXPLORER_URL = "https://ropsten.etherscan.io/tx/"
    const val TRX_T_EXPLORER_URL = "https://shasta.tronscan.org/#/transaction/"
    const val XLM_T_EXPLORER_URL = "https://testnet.steexp.com/tx/"
    const val XRP_T_EXPLORER_URL = "https://testnet.xrpl.org/transactions/"
    const val ADA_T_EXPLORER_URL = "https://explorer.cardano-testnet.iohkdev.io/en/transaction?id="
    */


    const val BTC_T_EXPLORER_URL = "https://live.blockcypher.com/btc-testnet/tx/"
    //    const val BTC_M_EXPLORER_URL = "https://live.blockcypher.com/btc/tx/"
    const val BTC_M_EXPLORER_URL = "https://www.blockchain.com/btc/tx/"
    const val ETH_T_EXPLORER_URL = "https://etherscan.io/tx/"
    const val TRX_T_EXPLORER_URL = "https://tronscan.io/#/transaction/"
    const val XLM_T_EXPLORER_URL = "https://steexp.com/tx/"
    const val XRP_T_EXPLORER_URL = "https://xrpscan.com/transactions/"
    const val ADA_T_EXPLORER_URL = "https://explorer.cardano.org/en/transaction?id="


    // firebase
    const val FCM_SAVE = ROOT_URL + "fcm/save"
    const val FCM_UPDATE_STATUS = ROOT_URL + "update/status"
    const val GET_FCM_STATUS = ROOT_URL + "fcm/get/status"
    //contact us
    const val SEND_EMAIL = ROOT_URL + "send/mail"

}