package io.tux.wallet.testnet.model.exchangeResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExchangeResponseModel(

	@field:SerializedName("code")
	val code: Any? = null,

	@field:SerializedName("data")
	val data: ExchangeData? = null,

	@field:SerializedName("message")
	val message: Any? = null
)

data class ExchangeDataItem(

	@field:SerializedName("exchangeGradePoAny?s")
	val exchangeGradePoints: Int? = null,

	@field:SerializedName("volume24hTo")
	val volume24hTo: Any? = null,

	@field:SerializedName("exchangeGrade")
	val exchangeGrade: Any? = null,

	@field:SerializedName("AggregatedData")
	val aggregatedData: AggregatedData? = null,

	@field:SerializedName("fromSymbol")
	val fromSymbol: Any? = null,

	@field:SerializedName("price")
	val price: Any? = null,

	@field:SerializedName("lastUpdateTs")
	val lastUpdateTs: Any? = null,

	@field:SerializedName("volume24h")
	val volume24h: Any? = null,

	@field:SerializedName("toSymbol")
	val toSymbol: Any? = null,

	@field:SerializedName("exchange")
	val exchange: Any? = null,

	@field:SerializedName("Exchanges")
	val exchanges: List<ExchangesItem?>? = null,

	@field:SerializedName("CoinInfo")
	val coinInfo: CoinInfo? = null
) : Serializable{}

data class CoinInfo(

	@field:SerializedName("TotalVolume24H")
	val totalVolume24H: Any? = null,

	@field:SerializedName("Internal")
	val internal: Any? = null,

	@field:SerializedName("BlockTime")
	val blockTime: Any? = null,

	@field:SerializedName("ImageUrl")
	val imageUrl: Any? = null,

	@field:SerializedName("MaxSupply")
	val maxSupply: Any? = null,

	@field:SerializedName("Algorithm")
	val algorithm: Any? = null,

	@field:SerializedName("Url")
	val url: Any? = null,

	@field:SerializedName("Name")
	val name: Any? = null,

	@field:SerializedName("TotalTopTierVolume24H")
	val totalTopTierVolume24H: Any? = null,

	@field:SerializedName("MktCapPenalty")
	val mktCapPenalty: Any? = null,

	@field:SerializedName("ProofType")
	val proofType: Any? = null,

	@field:SerializedName("NetHashesPerSecond")
	val netHashesPerSecond: Any? = null,

	@field:SerializedName("AssetLaunchDate")
	val assetLaunchDate: Any? = null,

	@field:SerializedName("FullName")
	val fullName: Any? = null,

	@field:SerializedName("Id")
	val id: Any? = null,

	@field:SerializedName("BlockNumber")
	val blockNumber: Any? = null,

	@field:SerializedName("BlockReward")
	val blockReward: Any? = null,

	@field:SerializedName("TotalCoinsMined")
	val totalCoinsMined: Any? = null
)

data class AggregatedData(

	@field:SerializedName("LASTTRADEID")
	val lASTTRADEID: Any? = null,

	@field:SerializedName("OPEN24HOUR")
	val oPEN24HOUR: Any? = null,

	@field:SerializedName("HIGHDAY")
	val hIGHDAY: Any? = null,

	@field:SerializedName("LOW24HOUR")
	val lOW24HOUR: Any? = null,

	@field:SerializedName("TOPTIERVOLUME24HOUR")
	val tOPTIERVOLUME24HOUR: Any? = null,

	@field:SerializedName("TOSYMBOL")
	val tOSYMBOL: Any? = null,

	@field:SerializedName("LASTVOLUME")
	val lASTVOLUME: Any? = null,

	@field:SerializedName("LASTMARKET")
	val lASTMARKET: Any? = null,

	@field:SerializedName("LOWHOUR")
	val lOWHOUR: Any? = null,

	@field:SerializedName("LASTUPDATE")
	val lASTUPDATE: Any? = null,

	@field:SerializedName("CHANGEPCTHOUR")
	val cHANGEPCTHOUR: Any? = null,

	@field:SerializedName("VOLUMEHOURTO")
	val vOLUMEHOURTO: Any? = null,

	@field:SerializedName("VOLUMEHOUR")
	val vOLUMEHOUR: Any? = null,

	@field:SerializedName("TOPTIERVOLUME24HOURTO")
	val tOPTIERVOLUME24HOURTO: Any? = null,

	@field:SerializedName("CHANGEDAY")
	val cHANGEDAY: Any? = null,

	@field:SerializedName("FLAGS")
	val fLAGS: Any? = null,

	@field:SerializedName("MEDIAN")
	val mEDIAN: Any? = null,

	@field:SerializedName("TYPE")
	val tYPE: Any? = null,

	@field:SerializedName("VOLUMEDAY")
	val vOLUMEDAY: Any? = null,

	@field:SerializedName("VOLUME24HOUR")
	val vOLUME24HOUR: Any? = null,

	@field:SerializedName("MARKET")
	val mARKET: Any? = null,

	@field:SerializedName("PRICE")
	val pRICE: Any? = null,

	@field:SerializedName("CHANGEPCTDAY")
	val cHANGEPCTDAY: Any? = null,

	@field:SerializedName("FROMSYMBOL")
	val fROMSYMBOL: Any? = null,

	@field:SerializedName("LASTVOLUMETO")
	val lASTVOLUMETO: Any? = null,

	@field:SerializedName("CHANGEPCT24HOUR")
	val cHANGEPCT24HOUR: Any? = null,

	@field:SerializedName("OPENDAY")
	val oPENDAY: Any? = null,

	@field:SerializedName("VOLUMEDAYTO")
	val vOLUMEDAYTO: Any? = null,

	@field:SerializedName("OPENHOUR")
	val oPENHOUR: Any? = null,

	@field:SerializedName("CHANGE24HOUR")
	val cHANGE24HOUR: Any? = null,

	@field:SerializedName("CHANGEHOUR")
	val cHANGEHOUR: Any? = null,

	@field:SerializedName("HIGH24HOUR")
	val hIGH24HOUR: Any? = null,

	@field:SerializedName("VOLUME24HOURTO")
	val vOLUME24HOURTO: Any? = null,

	@field:SerializedName("LOWDAY")
	val lOWDAY: Any? = null,

	@field:SerializedName("HIGHHOUR")
	val hIGHHOUR: Any? = null
)

data class ExchangeData(

	@field:SerializedName("Response")
	val response: Any? = null,

	@field:SerializedName("RateLimit")
	val rateLimit: RateLimit? = null,

	@field:SerializedName("Type")
	val type: Any? = null,

	@field:SerializedName("HasWarning")
	val hasWarning: Boolean? = null,

	@field:SerializedName("Data")
	val data: List<ExchangeDataItem?>? = null
)

data class ExchangesItem(

	@field:SerializedName("LASTTRADEID")
	val lASTTRADEID: Any? = null,

	@field:SerializedName("OPEN24HOUR")
	val oPEN24HOUR: Any? = null,

	@field:SerializedName("HIGHDAY")
	val hIGHDAY: Any? = null,

	@field:SerializedName("LOW24HOUR")
	val lOW24HOUR: Any? = null,

	@field:SerializedName("TOSYMBOL")
	val tOSYMBOL: Any? = null,

	@field:SerializedName("LASTVOLUME")
	val lASTVOLUME: Any? = null,

	@field:SerializedName("LOWHOUR")
	val lOWHOUR: Any? = null,

	@field:SerializedName("LASTUPDATE")
	val lASTUPDATE: Int? = null,

	@field:SerializedName("CHANGEPCTHOUR")
	val cHANGEPCTHOUR: Any? = null,

	@field:SerializedName("VOLUMEHOURTO")
	val vOLUMEHOURTO: Any? = null,

	@field:SerializedName("VOLUMEHOUR")
	val vOLUMEHOUR: Any? = null,

	@field:SerializedName("CHANGEDAY")
	val cHANGEDAY: Any? = null,

	@field:SerializedName("FLAGS")
	val fLAGS: Any? = null,

	@field:SerializedName("TYPE")
	val tYPE: Any? = null,

	@field:SerializedName("VOLUMEDAY")
	val vOLUMEDAY: Any? = null,

	@field:SerializedName("VOLUME24HOUR")
	val vOLUME24HOUR: Any? = null,

	@field:SerializedName("MARKET")
	val mARKET: Any? = null,

	@field:SerializedName("PRICE")
	val pRICE: Any? = null,

	@field:SerializedName("CHANGEPCTDAY")
	val cHANGEPCTDAY: Any? = null,

	@field:SerializedName("FROMSYMBOL")
	val fROMSYMBOL: Any? = null,

	@field:SerializedName("LASTVOLUMETO")
	val lASTVOLUMETO: Any? = null,

	@field:SerializedName("CHANGEPCT24HOUR")
	val cHANGEPCT24HOUR: Any? = null,

	@field:SerializedName("OPENDAY")
	val oPENDAY: Any? = null,

	@field:SerializedName("VOLUMEDAYTO")
	val vOLUMEDAYTO: Any? = null,

	@field:SerializedName("OPENHOUR")
	val oPENHOUR: Any? = null,

	@field:SerializedName("CHANGE24HOUR")
	val cHANGE24HOUR: Any? = null,

	@field:SerializedName("CHANGEHOUR")
	val cHANGEHOUR: Any? = null,

	@field:SerializedName("HIGH24HOUR")
	val hIGH24HOUR: Any? = null,

	@field:SerializedName("VOLUME24HOURTO")
	val vOLUME24HOURTO: Any? = null,

	@field:SerializedName("LOWDAY")
	val lOWDAY: Any? = null,

	@field:SerializedName("HIGHHOUR")
	val hIGHHOUR: Any? = null
) : Serializable

data class RateLimit(
	val any: Any? = null
)
