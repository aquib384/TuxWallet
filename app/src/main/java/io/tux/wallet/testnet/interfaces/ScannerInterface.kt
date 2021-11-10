package io.tux.wallet.testnet.interfaces

interface  ScannerInterface {
    fun scanQRCallBack(  code: com.google.android.gms.vision.barcode.Barcode , type :Int) {}
}