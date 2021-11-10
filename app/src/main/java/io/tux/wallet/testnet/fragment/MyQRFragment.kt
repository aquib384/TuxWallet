package io.tux.wallet.testnet.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.WriterException
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.FragmentMyQRBinding
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import android.widget.Toast

import androidx.core.content.FileProvider

import android.content.Intent
import android.net.Uri
import java.lang.Exception
import android.content.pm.PackageInfo

import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.tux.wallet.testnet.utils.Constants.DIR_IMAGES
import io.tux.wallet.testnet.utils.Constants.DIR_NAME
import io.tux.wallet.testnet.utils.Utils
import android.provider.MediaStore.Images

import android.content.ContentValues
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.*


class MyQRFragment : Fragment(), View.OnClickListener {
    lateinit var binding :FragmentMyQRBinding
    lateinit var bitmap: Bitmap
    lateinit var qrgEncoder: QRGEncoder
    private lateinit var coin: CoinListModel
    private val REQUEST_CODE = 12
    private val TAG= MyQRFragment::class.java.simpleName
    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyQRBinding.inflate(layoutInflater)
        checkPermission()
        coin = arguments?.get(Constants.COIN) as CoinListModel
        binding.tvName.text = coin.symbol
        binding.tvMobile.text = coin.coin_name
        binding.tvEmail.text = coin.coin_add
        binding.ivClose.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        bitmap =Utils.getQr(requireActivity(),coin)
        binding.ivCode.setImageBitmap(bitmap)
        return binding.root
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_close -> findNavController().popBackStack()
            R.id.btn_share -> saveBitmap(bitmap)
        }
    }

    fun saveBitmap(bitmap: Bitmap)
    {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        var fileName = coin.symbol+"_"+ts
        Log.e("filename",fileName)
//        shareBitmap()
//        Utils.createDirectoryAndSaveFile(bitmap,fileName)
        try {

            val dirs = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/" + resources.getString(R.string.app_name) + "/Images/"
            )
            if (!dirs.exists()) {
                dirs.mkdirs()
            }

            var file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/" + resources.getString(R.string.app_name) + "/Images/",
                fileName + ".jpeg"
            );

            Log.e(TAG, "" + file + file.exists());

            val out = FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.e("output",out.fd.toString())

        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }



    fun shareBitmap(fileName:String)
    {
        val icon: Bitmap = bitmap
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bytes = ByteArrayOutputStream()
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var f = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/" + resources.getString(R.string.app_name) + "/Images/",
            fileName + ".jpeg"
        );

        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"))
        startActivity(Intent.createChooser(share, "Share Image"))
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) + ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )+ ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
            {

            }
            else {
                makeRequest()
            }
        } else {

        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] !=PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")

                }
            }
        }
    }

}