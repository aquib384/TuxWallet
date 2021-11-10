package io.tux.wallet.testnet.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.zxing.WriterException
import com.mynameismidori.currencypicker.ExtendedCurrency
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.PinLoginActivity
import io.tux.wallet.testnet.adapter.CoinsAdapter
import io.tux.wallet.testnet.databinding.DialogComingSoonBinding
import io.tux.wallet.testnet.databinding.DialogMarketAutocompleteBinding
import io.tux.wallet.testnet.databinding.DialogSendReceiptBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.interfaces.ScannerInterface
import io.tux.wallet.testnet.model.coins.BalanceResponseModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.wallet.AdaDetails
import io.tux.wallet.testnet.model.wallet.CoinAddressItem
import io.tux.wallet.testnet.utils.Constants.ADAADDRES
import io.tux.wallet.testnet.utils.Constants.ADA_ADDRES_DETAILS
import io.tux.wallet.testnet.utils.Constants.ADA_MNEMONIC
import io.tux.wallet.testnet.utils.Constants.ADA_PHRASE
import io.tux.wallet.testnet.utils.Constants.ADA_WALLET_ID
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.IS_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_AAVE
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_BAND
import io.tux.wallet.testnet.utils.Constants.KEY_BTC
import io.tux.wallet.testnet.utils.Constants.KEY_ETH
import io.tux.wallet.testnet.utils.Constants.KEY_FIL
import io.tux.wallet.testnet.utils.Constants.KEY_LINK
import io.tux.wallet.testnet.utils.Constants.KEY_LTC
import io.tux.wallet.testnet.utils.Constants.KEY_SUSHI
import io.tux.wallet.testnet.utils.Constants.KEY_TRX
import io.tux.wallet.testnet.utils.Constants.KEY_UNI
import io.tux.wallet.testnet.utils.Constants.KEY_XLM
import io.tux.wallet.testnet.utils.Constants.KEY_XRP
import io.tux.wallet.testnet.utils.Constants.VALIDATE
import io.tux.wallet.testnet.utils.Urls.ADA_T_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.BTC_M_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.ETH_T_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.LTC_M_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.TRX_T_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.XLM_T_EXPLORER_URL
import io.tux.wallet.testnet.utils.Urls.XRP_T_EXPLORER_URL
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.and
import kotlin.random.Random


object Utils {
    @SuppressLint("StaticFieldLeak")
    lateinit var coinsDialog: BottomSheetDialog


    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun hideKeyboard(activity: Activity, editText: EditText) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (ignored: java.lang.Exception) {
        }
    }

    fun showKeyboard(activity: Activity, editText: EditText?) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isDarkMode(context: Context): Boolean {
        val nightModeFlags: Int = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        var isDarkMode = false
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> isDarkMode = true
            Configuration.UI_MODE_NIGHT_NO -> isDarkMode = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> isDarkMode = false
        }
        return isDarkMode
    }


    fun showInputMethod(view: View, context: Context?) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }


    fun disableEditText(editText: TextInputEditText) {
        editText.apply {
            isFocusable = false
            isEnabled = false
            isCursorVisible = false
            isFocusableInTouchMode = false
        }
    }

    fun enableEditText(editText: TextInputEditText) {
        editText.apply {
            isFocusable = true
            isEnabled = true
            isCursorVisible = true
            isFocusableInTouchMode = true
        }
    }


    fun openBrowser(context: Context, url: String) {
        val result = kotlin.runCatching {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
        result.onFailure {
            it.message?.let { it1 -> showToast(context, it1) }
        }
    }

    fun decodeStringBase64(coded: String): String? {
        val valueDecoded = Base64.decode(coded.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT);
        return String(valueDecoded)
    }

    fun encodeStringToBase64(text: String): String? {
        val data = text.toByteArray(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }


    fun getDrawableRes(context: Context, name: String): Int {
        val packageName = context.packageName
        return context.resources.getIdentifier(name, "drawable", packageName)
    }


/*
    @RequiresApi(Build.VERSION_CODES.O)
    fun vibratePhone(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }
*/


    fun setResultForScanner(
        context: Context, type: Int,
        code: com.google.android.gms.vision.barcode.Barcode,
        tag: String, scannnerInterface: ScannerInterface
    ) {
        scannnerInterface.scanQRCallBack(code, type)

        when (type) {
            Barcode.CONTACT_INFO -> Log.i(
                tag,
                code.contactInfo.title
            )
            Barcode.EMAIL -> Log.i(tag, code.email.address)
            Barcode.ISBN -> Log.i(tag, code.rawValue)
            Barcode.PHONE -> Log.i(tag, code.phone.number)
            Barcode.PRODUCT -> Log.i(tag, code.rawValue)
            Barcode.SMS -> Log.i(tag, code.sms.message)
            Barcode.TEXT -> {
                Log.i(tag, "text" + code.rawValue)

            }
            Barcode.URL -> {
                Log.i(tag, "url: " + code.url.url)
                Utils.goToUrl(context, code.url.url.toString())
            }
            Barcode.WIFI -> Log.i(
                tag,
                "wifi ssid" + code.wifi.ssid
            )
            Barcode.GEO -> Log.i(
                tag,
                code.geoPoint.lat.toString() + ":" + code.geoPoint.lng
            )
            Barcode.CALENDAR_EVENT -> Log.i(
                tag,
                code.calendarEvent.description
            )
            Barcode.DRIVER_LICENSE ->
                Log.i(tag, code.driverLicense.licenseNumber)

            else -> Log.i(tag, code.rawValue)
        }

    }


    fun goToUrl(context: Context, url: String) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            // Open URL
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        } else {
            Toast.makeText(context, "Invalid Url", Toast.LENGTH_LONG).show()
        }
    }


    fun showComingSoonDialog(
        context: Context
    ) {
        val dialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)
        val cBinding: DialogComingSoonBinding =
            DialogComingSoonBinding.inflate(LayoutInflater.from(context))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(cBinding.root)
        cBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    fun getDoubleValueFromString(num: String) = String.format("%.2f", num.toDouble())
    fun getDoubleValueFromString6(num: String) = String.format("%.6f", num.toDouble())

    fun getRandomNumber(start: Int, end: Int): Int {
        return Random.nextInt(start, end)
    }

    fun getCurrencySymbol(code: String): String {
        return ExtendedCurrency.getCurrencyByISO(code).symbol.toString()
    }

    fun gotoPin(context: Context, from: String): Intent? {
        val i = Intent(context, PinLoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra("from", from)
        if (from != "home") {
            context.startActivity(i)
            return null
        }
//            val fm = supportFragmentManager
//            val pinDialog = PinUnlockDialog(this)
//                pinDialog.show(fm,"unlock")
        return i
    }

    fun callValidationScreen(context: Context, tag: String? = VALIDATE) =
        Intent(context, PinLoginActivity::class.java).apply {
            putExtra("from", tag)
        }
//            val fm = supportFragmentManager
//            val pinDialog = PinUnlockDialog(this)
//                pinDialog.show(fm,"unlock")


    fun getXrpToTag(): String? {
        var d = Date().time
        var s = SimpleDateFormat(" HMSS")
        return s.format(d)
    }


    @SuppressLint("SetTextI18n")
    fun showTxnCompleteDialog(
        context: Context,
        toaddr: String,
        frmAddr: String?,
        fees: String,
        time: String,
        amount: String,
        nonce: String,
        coin: CoinListModel,
        typ: String
    ) {

        var txnDialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)

        var cBinding: DialogSendReceiptBinding =
            DialogSendReceiptBinding.inflate(LayoutInflater.from(context))
        txnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        txnDialog.setContentView(cBinding.root)
        cBinding.tvAmt.text = amount + " " + coin.symbol
        cBinding.tvPageTitle.text = typ + " " + coin.symbol
        cBinding.tvDate.text = time
        cBinding.ivClose.visibility = View.GONE
        if (nonce.isEmpty()) {
            cBinding.linNonce.visibility = View.GONE
        } else {
            cBinding.tvNonce.text = "#$nonce"
        }
        if (fees.isEmpty()) {
            cBinding.linFee.visibility = View.GONE
        } else {
            cBinding.tvFee.text = fees
        }
        if (toaddr.isEmpty()) {
            cBinding.linTo.visibility = View.GONE
        } else {
            cBinding.tvTo.text = toaddr
        }
        if (frmAddr?.isEmpty() == true) {
            cBinding.linFrom.visibility = View.GONE
        } else {
            cBinding.tvFrom.text = frmAddr
        }

        if (!txnDialog.isShowing)
            txnDialog.show()

    }

    fun showCoinsDialog(
        context: Context,
        coinsList: List<CoinListModel>,
        coinInterface: CoinInterface,
        isSend: Boolean
    ) {
        coinsDialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)

        var cBinding: DialogMarketAutocompleteBinding =
            DialogMarketAutocompleteBinding.inflate(LayoutInflater.from(context))
        coinsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        coinsDialog.setContentView(cBinding.root)
        val cAdapter: CoinsAdapter = CoinsAdapter(context, coinsList, coinInterface, isSend)
        cBinding.listview.apply {
//            Log.e("utilsadapter",coinsList.size .toString())
            layoutManager = LinearLayoutManager(context)
            adapter = cAdapter
        }

        coinsDialog.show()
    }

    fun dismissCoinsDialog() {
        coinsDialog.dismiss()
    }


    fun getHashMap(keyList: ArrayList<String>): HashMap<String?, Any?> {
        val map = java.util.HashMap<String?, Any?>()
        try {

            for (i in 0 until keyList.size) {
                map[(i + 1).toString()] = keyList[i]

            }
            Log.e("map", map.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }


    fun getHexString(byteArray: ByteArray, withPrefix: Boolean): String {
        return byteArray.toHexString(withPrefix)
    }

    private fun ByteArray.toHexString(withPrefix: Boolean = true): String {
        val stringBuilder = StringBuilder()
        if (withPrefix) {
            stringBuilder.append("0x")
        }
        for (element in this) {
            stringBuilder.append(String.format("%02x", element and 0xFF.toByte()))
        }
        return stringBuilder.toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun createDatefromTimeStamp(timestamp: String): String {
        var dv: Long = if (timestamp.length > 10)
            timestamp.toLong()
        else
            java.lang.Long.valueOf(timestamp) * 1000
        val df = Date(dv)
//        val vv = SimpleDateFormat("MM dd, yyyy hh:mma").format(df)
//        Log.e("timestamp",timestamp.toString())
//        val d = Date(timestamp).time
//        Log.e("datesss",d.toString())
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.getDefault())
        Log.e("datessss", sdf.format(df).toString())
        return sdf.format(df).toString()
    }


    fun getDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.getDefault())
        return df.format(c)
    }


    fun copyTextToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        showToast(context, context.resources.getString(R.string.text_copied))
    }


    fun getQr(activity: Activity, coin: CoinListModel): Bitmap {
        var qrgEncoder: QRGEncoder
        var args = Bundle()
        args.putSerializable(COIN, coin)


//        val width: Int = binding.ivCode.width
//        val height: Int = binding.ivCode.height

        val display: Display? = activity.windowManager?.defaultDisplay
        val point = Point()
        display?.getSize(point)
        val width: Int = point.x
        val height: Int = point.y
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        qrgEncoder = QRGEncoder(coin.coin_add, args, QRGContents.Type.TEXT, dimen)
        try {
            // getting our qrcode in the form of bitmap.
            var bitmap = qrgEncoder.bitmap

        } catch (e: WriterException) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString())
        }
        return qrgEncoder.bitmap
    }


    fun createDirectoryAndSaveFile(imageToSave: Bitmap, fileName: String) {
        val direct = File(Environment.getExternalStorageDirectory().toString() + Constants.DIR_NAME)
        if (!direct.exists()) {
            val imgFolder = File(Constants.DIR_IMAGES)
            imgFolder.mkdirs()
        }
        val file = File(Constants.DIR_IMAGES, fileName)
        Log.e("file", file.absolutePath)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun timeStampfromCreatedAt(time: String): Long? {
        var d: Date? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val i = Instant.parse(time)
                d = Date.from(i)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return d?.time
    }

    fun getIndexOfCoin(coin: String?, coinList: ArrayList<CoinListModel>): Int {
        var index = 0
        for (i in 0 until coinList.size) {
            if (coinList[i].symbol == coin) {
                index = i
            }
        }
        return index
    }

    fun coinObjectToAddressMap(
        cList: ArrayList<CoinListModel>,
        sharedPref: SharedPref
    ): JSONObject {
        var jsonArray = JSONArray()
        var jsonObject = JSONObject()
        try {

            Log.e("clist", cList.toString())
            cList.forEach { coinListModel ->

                var jarr = JSONArray()
                var jObj = JSONObject()
                if (coinListModel.adaDetail.adaCoinAddressDetails.isNotEmpty()) {
                    coinListModel.adaDetail.adaCoinAddressDetails.forEach {
                        jObj.put(ADAADDRES, it)
                    }
                    jarr.put(jsonObject)
                }

                jsonObject.put(ADA_ADDRES_DETAILS, jObj)
                jsonObject.put(ADA_MNEMONIC, coinListModel.adaDetail.adaMnemonic)
                jsonObject.put(ADA_PHRASE, coinListModel.adaDetail.adaPhrase)
                jsonObject.put(ADA_WALLET_ID, coinListModel.adaDetail.adaWalletId)
                jsonObject.put(Constants.ADDRESS, coinListModel.coin_add)
                jsonObject.put(COIN, coinListModel.symbol)
                jsonObject.put(Constants.CONTRACT_ID, coinListModel.contract_id)
                jsonObject.put(Constants.DEVICE_ID, sharedPref.getDeviceId())
                jsonObject.put(Constants.ID, "")
                jsonObject.put(Constants.SECRET_KEY, coinListModel.secret_key)
                jsonObject.put(Constants.PRIVATE_KEY, coinListModel.coin_key)
                jsonObject.put(Constants.USER_DETAILS_ID, sharedPref.getWalletId())
                if (coinListModel.symbol == KEY_ADA)
                    jsonObject.put(IS_ADA, "YES")
                else
                    jsonObject.put(IS_ADA, "NO")
//              Log.e("jsonOb",jsonObject.toString())
                jsonArray.put(jsonObject)
                Log.e("arr", "" + jsonArray)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonObject
    }


    fun setDefaultCurrency(context: Context, sharedPref: SharedPref) {
        val tm = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
        val currency: ExtendedCurrency? = ExtendedCurrency.getCurrencyByISO("JPY")
        Log.e("curency", currency?.code + "--" + currency?.symbol)

        if (sharedPref.getCurrencySymbol().isNullOrEmpty() && sharedPref.getCurrencyCode()
                .isNullOrEmpty()
        ) {
            currency?.code?.let { sharedPref.setCurrencyCode(it) }
            currency?.symbol?.let { sharedPref.setCurrencySymbol(it) }
        }

    }

    fun getBalanceData(data: Any): Any {
        var bal: Any
        var d: Any? = if (data is BalanceResponseModel.Data)
            data.balance
        else
            data.toString()
        when (d) {
            is String -> {
                Log.e("bal", "isString")
                bal = when (d) {
                    d.isNullOrEmpty() -> 0.0
                    d.equals("null", true) -> 0.0
                    else -> d
                }
            }
            is Double -> {

                bal = d.toDouble()

                Log.e("bal", "isDouble" + "--" + bal)
            }
            is Long -> {
                bal = d.toDouble()
                Log.e("bal", "isLong")
            }
            else -> {
                bal = 0.0
                Log.e("bal", "is null")
            }
        }
        Log.e("data", d.toString() + "\n" + bal)
        return bal
    }


    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    fun checkAdaAdrreses(
        address: String,
        addList: List<CoinAddressItem.AdaCoinAddressDetail>
    ): Boolean {
        var flag = 0
        addList.forEach {
            if (address.equals(it.adaAddress, true))
                flag = 1

        }
        return when (flag) {
            0 -> false
            else -> true
        }
    }

    fun checkArrayAddress(address: String, jsonArray: JSONArray): Boolean {
        var flag = 0
        for (i in 0 until jsonArray.length()) {
            if (jsonArray[i].toString().equals(address, true)) {
                flag = 1
            }
        }

        return when (flag) {
            0 -> false
            else -> true
        }
    }


    fun checkBtcOutputAddress(
        address: String,
        list: List<BTCTransactionHistoryModel.Data.Tx.Output>
    ): Boolean {
        var flag = 0
        list.forEach {
            var addList = it.addresses
            for (i in addList?.indices!!) {
                Log.e("btc_address", i.toString() + "--" + addList[i])
                if (addList[i].equals(address, true)) {
                    flag = 1
                }
            }

        }

        return when (flag) {
            0 -> false
            else -> true
        }
    }

    fun checkBtcSentAddress(
        address: String,
        list: List<BTCTransactionHistoryModel.Data.Tx.Output>
    ): Boolean {
        var flag = 0
        list.forEach {
//            var addList = it.addresses
//            for (i in addList.indices) {
//                Log.e("btc_address", i.toString() + "--" + addList[i])
            if (it.addresses?.get(0)?.equals(address, true) == true) {
                flag = 1
//                }
            }

        }

        return when (flag) {
            0 -> false
            else -> true
        }
    }

    fun checkBtcInputAddress(
        address: String,
        list: List<BTCTransactionHistoryModel.Data.Tx.Input>
    ): Boolean {
        var flag = false
        list.forEach {
            val addList = it.addresses
            if (!addList.isNullOrEmpty())
                for (i in addList.indices) {
                    if (addList[i].equals(address, true)) {
                        flag = true
                    }
                }

        }
        return flag
    }

    fun getInputAddress(
        list: List<BTCTransactionHistoryModel.Data.Tx.Input>
    ): String {
        var address = ""
        list.forEach {
            val addList = it.addresses
            if (!addList.isNullOrEmpty())
                for (i in addList.indices) {
                    address = addList[i] + "\n"

                }

        }

        return address
    }

    fun getOutputAddress(
        list: List<BTCTransactionHistoryModel.Data.Tx.Output>
    ): String {
        var address = ""
        list.forEach {
            var addList = it.addresses
            for (i in addList?.indices!!) {
                address = addList[i] + "\n"

            }

        }

        return address
    }


    fun goToExplorer(context: Context, data: String, coin: CoinListModel) {
        var url = when (coin.symbol) {
            KEY_BTC -> BTC_M_EXPLORER_URL
            KEY_LTC -> LTC_M_EXPLORER_URL
            KEY_ETH -> ETH_T_EXPLORER_URL
            KEY_LINK -> ETH_T_EXPLORER_URL
            KEY_SUSHI -> ETH_T_EXPLORER_URL
            KEY_AAVE -> ETH_T_EXPLORER_URL
            KEY_UNI -> ETH_T_EXPLORER_URL
            KEY_BAND -> ETH_T_EXPLORER_URL
            KEY_FIL -> ETH_T_EXPLORER_URL
            KEY_ADA -> ADA_T_EXPLORER_URL
            KEY_XRP -> XRP_T_EXPLORER_URL
            KEY_XLM -> XLM_T_EXPLORER_URL
            KEY_TRX -> TRX_T_EXPLORER_URL
            else -> ""
        }
        if (url.isNotEmpty()) {
            var urls = url + data
            Log.e("urls", urls)
            if (Patterns.WEB_URL.matcher(urls).matches()) {
                // Open URL
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urls))
                context.startActivity(browserIntent)
            } else {
                Toast.makeText(context, "Invalid Url", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getSelectedLanguage(sharedPref: SharedPref) = when (sharedPref.getLanguage()) {
        1 -> "japanese"
        2 -> "chinese_simplified"
        else -> "english"
    }

    fun getLanguageSymbol(sharedPref: SharedPref) = when (sharedPref.getLanguage()) {
        1 -> "ja"
        2 -> "zh"
        else -> "en"
    }

    fun getStringCoinsList(coinList: ArrayList<CoinListModel>): List<String> {

        val sList = ArrayList<String>()
//        coinList.forEach {
//            sList.add(it.symbol)
//        }
        sList.add(KEY_BTC)
        sList.add(KEY_ETH)
        sList.add(Constants.KEY_BCH)
        sList.add(KEY_LINK)
        sList.add(KEY_LTC)
        sList.add(Constants.KEY_XTZ)
        sList.add(KEY_ADA)
        sList.add(Constants.KEY_DOT)
        sList.add(KEY_UNI)
        sList.add(KEY_AAVE)
        sList.add(KEY_SUSHI)
        sList.add(KEY_FIL)
        sList.add(KEY_XRP)
        sList.add(Constants.KEY_EOS)
        sList.add(KEY_BAND)
        sList.add(Constants.KEY_ATOM)
        sList.add(KEY_TRX)
        sList.add(Constants.KEY_NEO)
        sList.add(Constants.KEY_ALGO)
        sList.add(KEY_XLM)

        Log.e("sssssssss", sList.toString())
        return sList
    }


    fun getEthIndex(list: ArrayList<CoinAddressItem>): Int {
        var index = 0
        for (i in list.indices) {
            if (list[i].coin == KEY_ETH) {
                index = i
            }
        }
        return index
    }

    fun getEthData(list: ArrayList<CoinListModel>): CoinListModel {
        var ethData = list[0]
        for (i in list.indices) {
            if (list[i].symbol == KEY_ETH) {
                ethData = list[i]
            }
        }
        return ethData
    }

    fun ArrayList<CoinListModel>.sortCoins() {
        this.sortByDescending {
            it.raw.currencies[0].data.PRICE.toString().toDouble()
        }
        val last = this.last()
        this.removeLast()
        this.add(0, last)
    }


    fun getSavedAddress(coin: CoinListModel, sharedPref: SharedPref): CoinListModel {
        try {
            val gson = Gson()
            val json: String? = sharedPref.getAddresses()
            val type: Type =
                object : com.google.common.reflect.TypeToken<ArrayList<CoinAddressItem?>?>() {}.type
            val sList = gson.fromJson<Any>(json, type) as ArrayList<CoinAddressItem>
            sList.forEach {
                Log.e("slist---", it.coin + "---" + it.toString())
            }
            Log.e("slist", sList.toString())

            for (i in 0 until sList.size) {
                val sItem = sList[i]
                if (sItem.coin.equals(coin.symbol, true)) {
                    val adaDetails = AdaDetails(
                        sItem.adaCoinAddressDetails,
                        sItem.adaMnemonic,
                        sItem.adaPhrase,
                        sItem.adaWalletId
                    )
                    coin.adaDetail = adaDetails
                    coin.coin_id = sItem.id
                    coin.coin_name = coin.coin_name
                    coin.coin_add = sItem.address
                    coin.secret_key = sItem.secretKey
                    coin.contract_id = sItem.contractId
                    coin.coin_key = sItem.privateKey

                    Log.e("coinListststst", coin.coin_name + "--" + coin.toString())

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return coin
    }


    fun setImgDrawableGlide(context: Context, element: CoinListModel, v: View): Drawable? {
        var dResource: Drawable? = null

        Glide.with(context)
            .load(element.img)
            .into(object : CustomTarget<Drawable?>(70, 70) {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    when (v) {
                        is EditText -> {

                            v.setCompoundDrawablesWithIntrinsicBounds(
                                resource,
                                null,
                                context?.resources.getDrawable(R.drawable.ic_arrow_drop_down, null),
                                null
                            )
                        }
                        is MaterialTextView -> {
                            v.setCompoundDrawablesWithIntrinsicBounds(
                                resource,
                                null,
                                null,
                                null
                            )
                        }
                    }
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {
                    dResource = placeholder
                }
            })
        return dResource
    }
}

