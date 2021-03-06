package io.tux.wallet.testnet.model.coinResponseModel

import java.io.Serializable

data class CoinDataItem(
    val CHANGE24HOUR: Any,
    val CHANGEDAY: Any,
    val CHANGEHOUR: Any,
    val CHANGEPCT24HOUR: Any,
    val CHANGEPCTDAY: Any,
    val CHANGEPCTHOUR: Any,
    val CONVERSIONSYMBOL: Any,
    val CONVERSIONTYPE: Any,
    val FLAGS: Any,
    val FROMSYMBOL: Any,
    val HIGH24HOUR: Any,
    val HIGHDAY: Any,
    val HIGHHOUR: Any,
    val IMAGEURL: Any,
    val LASTMARKET: Any,
    val LASTTRADEID: Any,
    val LASTUPDATE: Any,
    val LASTVOLUME: Any,
    val LASTVOLUMETO: Any,
    val LOW24HOUR: Any,
    val LOWDAY: Any,
    val LOWHOUR: Any,
    val MARKET: Any,
    val MEDIAN: Any,
    val MKTCAP: Any,
    val MKTCAPPENALTY: Any,
    val OPEN24HOUR: Any,
    val OPENDAY: Any,
    val OPENHOUR: Any,
    var PRICE: Any,
    val SUPPLY: Any,
    val TOPTIERVOLUME24HOUR: Any,
    val TOPTIERVOLUME24HOURTO: Any,
    val TOSYMBOL: Any,
    val TOTALTOPTIERVOLUME24H: Any,
    val TOTALTOPTIERVOLUME24HTO: Any,
    val TOTALVOLUME24H: Any,
    val TOTALVOLUME24HTO: Any,
    val TYPE: Any,
    val VOLUME24HOUR: Any,
    val VOLUME24HOURTO: Any,
    val VOLUMEDAY: Any,
    val VOLUMEDAYTO: Any,
    val VOLUMEHOUR: Any,
    val VOLUMEHOURTO: Any



):Serializable{
    override fun toString(): String {
        return "CoinDataItem(CHANGE24HOUR=$CHANGE24HOUR, CHANGEDAY=$CHANGEDAY, CHANGEHOUR=$CHANGEHOUR, CHANGEPCT24HOUR=$CHANGEPCT24HOUR, CHANGEPCTDAY=$CHANGEPCTDAY, CHANGEPCTHOUR=$CHANGEPCTHOUR, CONVERSIONSYMBOL=$CONVERSIONSYMBOL, CONVERSIONTYPE=$CONVERSIONTYPE, FLAGS=$FLAGS, FROMSYMBOL=$FROMSYMBOL, HIGH24HOUR=$HIGH24HOUR, HIGHDAY=$HIGHDAY, HIGHHOUR=$HIGHHOUR, IMAGEURL=$IMAGEURL, LASTMARKET=$LASTMARKET, LASTTRADEID=$LASTTRADEID, LASTUPDATE=$LASTUPDATE, LASTVOLUME=$LASTVOLUME, LASTVOLUMETO=$LASTVOLUMETO, LOW24HOUR=$LOW24HOUR, LOWDAY=$LOWDAY, LOWHOUR=$LOWHOUR, MARKET=$MARKET, MEDIAN=$MEDIAN, MKTCAP=$MKTCAP, MKTCAPPENALTY=$MKTCAPPENALTY, OPEN24HOUR=$OPEN24HOUR, OPENDAY=$OPENDAY, OPENHOUR=$OPENHOUR, PRICE=$PRICE, SUPPLY=$SUPPLY, TOPTIERVOLUME24HOUR=$TOPTIERVOLUME24HOUR, TOPTIERVOLUME24HOURTO=$TOPTIERVOLUME24HOURTO, TOSYMBOL=$TOSYMBOL, TOTALTOPTIERVOLUME24H=$TOTALTOPTIERVOLUME24H, TOTALTOPTIERVOLUME24HTO=$TOTALTOPTIERVOLUME24HTO, TOTALVOLUME24H=$TOTALVOLUME24H, TOTALVOLUME24HTO=$TOTALVOLUME24HTO, TYPE=$TYPE, VOLUME24HOUR=$VOLUME24HOUR, VOLUME24HOURTO=$VOLUME24HOURTO, VOLUMEDAY=$VOLUMEDAY, VOLUMEDAYTO=$VOLUMEDAYTO, VOLUMEHOUR=$VOLUMEHOUR, VOLUMEHOURTO=$VOLUMEHOURTO)"
    }
}