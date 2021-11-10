//package io.tux.wallet.testnet.model.coinResponse
//
//data class CoinListResponse(
//    val code: Int,
//    val `data`: Data,
//    val message: String
//) {
//    data class Data(
//        val display: List<Display>,
//        val raw: List<Raw>
//    ) {
//        data class Display(
//            val coin: String,
//            val coinFullName: String,
//            val coinStatus: String,
//            val currencies: List<Currency>,
//            val stakeStatus: String
//        ) {
//            data class Currency(
//                val currency: String,
//                val `data`: Data
//            ) {
//                data class Data(
//                    val CHANGE24HOUR: String,
//                    val CHANGEDAY: String,
//                    val CHANGEHOUR: String,
//                    val CHANGEPCT24HOUR: String,
//                    val CHANGEPCTDAY: String,
//                    val CHANGEPCTHOUR: String,
//                    val CIRCULATINGSUPPLY: String,
//                    val CIRCULATINGSUPPLYMKTCAP: String,
//                    val CONVERSIONSYMBOL: String,
//                    val CONVERSIONTYPE: String,
//                    val FROMSYMBOL: String,
//                    val HIGH24HOUR: String,
//                    val HIGHDAY: String,
//                    val HIGHHOUR: String,
//                    val IMAGEURL: String,
//                    val LASTMARKET: String,
//                    val LASTTRADEID: String,
//                    val LASTUPDATE: String,
//                    val LASTVOLUME: String,
//                    val LASTVOLUMETO: String,
//                    val LOW24HOUR: String,
//                    val LOWDAY: String,
//                    val LOWHOUR: String,
//                    val MARKET: String,
//                    val MKTCAP: String,
//                    val MKTCAPPENALTY: String,
//                    val OPEN24HOUR: String,
//                    val OPENDAY: String,
//                    val OPENHOUR: String,
//                    val PRICE: String,
//                    val SUPPLY: String,
//                    val TOPTIERVOLUME24HOUR: String,
//                    val TOPTIERVOLUME24HOURTO: String,
//                    val TOSYMBOL: String,
//                    val TOTALTOPTIERVOLUME24H: String,
//                    val TOTALTOPTIERVOLUME24HTO: String,
//                    val TOTALVOLUME24H: String,
//                    val TOTALVOLUME24HTO: String,
//                    val VOLUME24HOUR: String,
//                    val VOLUME24HOURTO: String,
//                    val VOLUMEDAY: String,
//                    val VOLUMEDAYTO: String,
//                    val VOLUMEHOUR: String,
//                    val VOLUMEHOURTO: String
//                )
//            }
//        }
//
//        data class Raw(
//            val coin: String,
//            val currencies: List<Currency>
//        ) {
//            data class Currency(
//                val currency: String,
//                val `data`: Data
//            ) {
//                data class Data(
//                    val CHANGE24HOUR: Double,
//                    val CHANGEDAY: Double,
//                    val CHANGEHOUR: Double,
//                    val CHANGEPCT24HOUR: Double,
//                    val CHANGEPCTDAY: Double,
//                    val CHANGEPCTHOUR: Double,
//                    val CIRCULATINGSUPPLY: Double,
//                    val CIRCULATINGSUPPLYMKTCAP: Double,
//                    val CONVERSIONSYMBOL: String,
//                    val CONVERSIONTYPE: String,
//                    val FLAGS: String,
//                    val FROMSYMBOL: String,
//                    val HIGH24HOUR: Double,
//                    val HIGHDAY: Double,
//                    val HIGHHOUR: Double,
//                    val IMAGEURL: String,
//                    val LASTMARKET: String,
//                    val LASTTRADEID: String,
//                    val LASTUPDATE: Int,
//                    val LASTVOLUME: Double,
//                    val LASTVOLUMETO: Double,
//                    val LOW24HOUR: Double,
//                    val LOWDAY: Double,
//                    val LOWHOUR: Double,
//                    val MARKET: String,
//                    val MEDIAN: Double,
//                    val MKTCAP: Long,
//                    val MKTCAPPENALTY: Int,
//                    val OPEN24HOUR: Double,
//                    val OPENDAY: Double,
//                    val OPENHOUR: Double,
//                    val PRICE: Double,
//                    val SUPPLY: Int,
//                    val TOPTIERVOLUME24HOUR: Double,
//                    val TOPTIERVOLUME24HOURTO: Double,
//                    val TOSYMBOL: String,
//                    val TOTALTOPTIERVOLUME24H: Double,
//                    val TOTALTOPTIERVOLUME24HTO: Double,
//                    val TOTALVOLUME24H: Double,
//                    val TOTALVOLUME24HTO: Double,
//                    val TYPE: String,
//                    val VOLUME24HOUR: Double,
//                    val VOLUME24HOURTO: Double,
//                    val VOLUMEDAY: Double,
//                    val VOLUMEDAYTO: Double,
//                    val VOLUMEHOUR: Double,
//                    val VOLUMEHOURTO: Double
//                )
//            }
//        }
//    }
//}