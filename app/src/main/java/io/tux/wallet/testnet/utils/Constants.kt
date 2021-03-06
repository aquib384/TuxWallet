package io.tux.wallet.testnet.utils

object Constants {
    var IS_CONNECTED = false
    var VALIDATE = "Validate"

    //    preferences keys
    val SHARED_PREF_KEY: String = "SharedPreferences"
    const val IS_LIGHT_THEME = "is_light_theme"
    const val RECOVERY = "recovery"
    const val IS_LOGIN_KEY = "is_login_key"
    const val IS_FIRST_TIME= "is_first_time"
    const val IS_OLD_USER = "is_old_user"
    const val ENTROPY_KEY = "key_entropy"
    const val ETH_ADDRESS_KEY = "eth_address_key"
    const val WIF_KEY = "wif_key"
    const val LOGOUT = "logout"
    const val BCH_WIF_KEY = "bch_wif_key"
    const val LTC_WIF_KEY = "ltc_wif_key"
    const val ETH_PRIVATE_KEY = "eth_private_key"
    const val BTC_ADDRESS_KEY = "btc_address_key"
    const val BCH_ADDRESS_KEY = "bch_address_key"
    const val LTC_ADDRESS_KEY = "ltc_address_private_key"
    const val CURRENCY = "currency"
    const val CURRENCY_SYMBOL = "currency_symbol"
    const val IS_PIN_ENABLE = "is_pin_enable"
    const val IS_NOTIFY_ENABLE = "is_notify_enable"
    const val IS_BIOMETRIC_ENABLE = "is_biometric_enable"
    const val SP_PIN_CODE_LOGIN = "SP_PIN_CODE_LOGIN"
    const val SP_KEY_LANGUAGE = "SP_KEY_LANGUAGE"
    const val WALLET_ADDRESSES ="wallet_address"
    const val FCM_TOKEN_ID ="fcm_token_id"
    const val MNEMONIC_LANGUGAGE ="mnemonic_language"

    // Header Keys
    const val CONTENT_TYPE = "Content-Type"
    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer"
    const val ACCEPT_LANGUAGE = "Accept-Language"
    const val USER = "tux"
    const val NONCE = "5XhnO7ptMZafAVvewHdv"
    const val HMAC_SECRET = "zL1l33JaQSdhX4BIbA76pYYmS3wpxgdDO1V0PLM+Oyc="

    // appKeys
    const val SPLASH = "SPLASH"
    const val MARKET = "CCMVDA"
    const val KEYLIST = "key_list"
    const val COINS = "coins"
    const val COIN = "coin"
    const val PAIR = "pair"
    const val COINS_MAP_PARAMETER = "coins map"
    const val FROM = "from"
    const val WALLET_PHRASE = "wallet_phrase"
    const val AGGREGATE = "aggregate"
    const val FSYM = "fsym"
    const val TSYM = "tsym"
    const val USD = "USD"
    const val JPY = "JPY"
    const val EUR = "EUR"
    const val GBP = "GBP"
    const val CNY = "CNY"
    const val INR = "INR"
    const val VERIFICATION = "VERIFICATION"
    const val EXTRA_PARAM = "extraParams"
    const val LIMIT = "limit"
    const val TIMESTAMP = "timestamp"
    const val WALLET_ID = "wallet_id"
    const val DIR_NAME = "/TuxWallet"
    const val DIR_IMAGES = "/images/address"
    const val TEST_NET_DERIVATION = "m/44\'/1\'/0\'/0/0"
    const val MAIN_NET_DERIVATION = "m/84\'/60\'/1\'/0/0"
    const val ACTIVE = "ACTIVE"
    const val INACTIVE = "INACTIVE"
    const val INDEX = "INDEX"
    const val BALANCE = "balance"


    // send to qr code request code
    const val QR_SCAN_REQUEST_CODE = "001"


    // api response keys
    const val RAW= "raw"
    const val DISPLAY= "display"
    const val BIOMETRIC= "BIOMETRIC"
    const val NON_BIOMETRIC= "NON_BIOMETRIC"
    const val DEVICE_ID= "deviceId"
    const val LOCK_TYPE= "lockType"
    const val MNEMONICS="mnemonics"
    const val MNEMONIC="mnemonic"
    const val WALLET_PIN= "walletPin"
    const val ADDRESS= "address"
    const val CONTRACT_ID= "contractId"
    const val ID= "id"
    const val SECRET_KEY= "secretKey"
    const val PRIVATE_KEY= "privateKey"
    const val USER_DETAILS_ID= "userDetailsId"
    const val FROM_ADDR= "fromAddr"
    const val TO_ADDR= "toAddr"
    const val TOADDRESS= "toAddress"
    const val FROMADDRESS= "fromAddress"
    const val AMOUNT= "amount"
    const val SORT= "sort"
    const val ASC= "ASC"
    const val DESC= "DESC"
    const val TO_ADDRESS= "to_address"
    const val FROM_ADDRESS= "from_address"
    const val SECRET_ADDRESS= "secret_address"
    const val DEST_ADDRESS= "destAddr"
    const val TOTAG= "toTag"
    const val SRC_ADDRESS= "srcAddr"
    const val SECRET= "secret"
    const val FROM_PRIVATE_KEY= "fromPrivateKey"
    const val FROMADDRES= "fromAddress"
    const val RECEIVER_ADDRESS= "receiverAddress"
    const val PASSPHRASE= "passphrase"
    const val ADAADDRES= "adaAddress"
    const val ADA_ADDRES_DETAILS= "adaCoinAddressDetails"
    const val ADA_MNEMONIC= "adaMnemonic"
    const val ADA_PHRASE= "adaPhrase"
    const val ADA_WALLET_ID= "adaWalletId"
    const val IS_ADA= "isAdaTypeCoin"
    const val TXN_ID= "txId"
    const val WALLET_DETAILS_ID= "walletDetailsId"
    const val URL= "url"
    const val COINSYMBOL= "coinSymbol"
    const val STAKESTATS= "stakeStats"
    const val TXSTATS= "txStats"
    const val DESCRIPTION= "description"
    const val EMAILID= "emailId"
    const val FULLNAME= "fullName"
    const val SUBJECT= "subject"
    const val FCMSTATUS= "fcmStatus"
    const val MSG= "msg"
    const val TOKENID= "tokenId"


    //Coins
    const val KEY_TUXC = "TUXC"
    const val KEY_BTC = "BTC"
    const val KEY_ETH = "ETH"
    const val KEY_BCH = "BCH"
    const val KEY_LINK = "LINK"
    const val KEY_LTC = "LTC"
    const val KEY_XTZ = "XTZ"
    const val KEY_ADA = "ADA"
    const val KEY_DOT = "DOT"
    const val KEY_UNI = "UNI"
    const val KEY_AAVE = "AAVE"
    const val KEY_SUSHI = "SUSHI"
    const val KEY_FIL = "FIL"
    const val KEY_XRP = "XRP"
    const val KEY_EOS = "EOS"
    const val KEY_BAND = "BAND"
    const val KEY_ATOM = "ATOM"
    const val KEY_TRX = "TRX"
    const val KEY_USDT = "USDT"
    const val KEY_NEO = "NEO"
    const val KEY_ALGO = "ALGO"
    const val KEY_XLM = "XLM"
    const val CHAINLINK = "CHAINLINK"
    const val UNISWAP = "UNISWAP"
    const val AAVE = "AAVE"
    const val SUSHISWAP = "SUSHISWAP"
    const val BANDTOKEN = "BANDTOKEN"


    //    const val UNI_PATH = "uniswap"
//    const val BAND_PATH = "band"
//    const val LINK_PATH = "link"
//    const val FILE_PATH = "file"
//    const val AAVE_PATH = "aave"
//    const val SUSHI_PATH = "sushi"
//    const val ETH_PATH = "eth"
    const val TRX_PATH = "tron"
    const val XLM_PATH = "steller"
    const val XRP_PATH = "ripple"
    const val BTC_PATH = "btc"
    const val LTC_PATH = "ltc"
    const val ADA_PATH = "ada"
    const val ERC20_PATH = "erc20"
    const val ERC20 = "ERC20"


    // Endpoints
    const val WITHDRAW = "withdraw"
    const val TRANSFER = "transfer"


    const val UPDATE_REQUEST_CODE = 3402
    const val VERIFY_REQUEST_CODE = 345






}