package io.tux.wallet.testnet.fragment

import android.Manifest
import android.content.Context.CAMERA_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.Camera.Parameters.FLASH_MODE_ON
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.HomeActivity
import io.tux.wallet.testnet.databinding.FragmentScanQRBinding
import io.tux.wallet.testnet.interfaces.ScannerInterface
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.FROM
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.TransactionViewModel
import java.lang.reflect.Field
import java.util.*

class ScanQRFragment : Fragment(), View.OnClickListener,
    SurfaceHolder.Callback, ScannerInterface {
    lateinit var binding: FragmentScanQRBinding
    var cameraSource: CameraSource? = null
    lateinit var barcodeDetector: BarcodeDetector
    private val TAG = "ScanQR"
    private val REQUEST_CODE = 10
    lateinit var animation: Animation
    var isFlashOn: Boolean = false
    lateinit var fromContext: String
    private val viewModel: TransactionViewModel by activityViewModels()
    private lateinit var coin: CoinListModel
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentScanQRBinding.inflate(layoutInflater)
        fromContext = arguments?.get(FROM).toString()
        coin = arguments?.get(Constants.COIN) as CoinListModel
        viewModel.setSelectedCoin(coin)
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.scan_animation)
        animation.start()
        binding.view.animation = animation
//        val isFlashAvailable: Boolean =
//            ApplicationProvider.getApplicationContext<Context>().getPackageManager()
//                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        mCameraManager = context?.getSystemService(CAMERA_SERVICE) as CameraManager?

        try {
            mCameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        checkPermission()
        setBarcodeScanner()
        binding.ivClose.setOnClickListener(this)
        binding.ivFlash.setOnClickListener(this)
        binding.ivGallery.setOnClickListener(this)
        binding.btnMyQr.setOnClickListener(this)

        return binding.root
    }


    private fun setBarcodeScanner() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.QR_CODE).build()
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(300, 300).build()
        if (cameraSource == null) {

            return;
        }


        binding.scannerView.holder.addCallback(this)

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                return
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>) {
                try {
                    var qr_codes = SparseArray<Barcode>()
                    qr_codes = p0.detectedItems
                    if (qr_codes != null) {
                        for (index in 0 until qr_codes.size()) {
                            val code = qr_codes.valueAt(index)
                            val type = qr_codes.valueAt(index).valueFormat
                            Utils.setResultForScanner(
                                requireContext(),
                                type,
                                code,
                                TAG,
                                this@ScanQRFragment
                            )
//                            animation.cancel()
//                            context?.let { Utils.vibratePhone(it) }
                            Log.e("scanned_code_detected", code.rawValue.toString())
//                            viewModel.setScanAdd(code.rawValue.toString())
//                            goToSendScreen(code)
                            return


                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }


    private fun chooseImageGallery() {
        resetScanner()
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun setFlash() {

        if (getFlash(cameraSource)) {
//            cameraFlash(cameraSource, Camera.Parameters.FLASH_MODE_OFF)
        } else {
//            cameraFlash(cameraSource,FLASH_MODE_ON)

        }
    }

    private fun cameraFlash(cameraSource: CameraSource?, mode: String) {
        val declaredFields: Array<Field> = CameraSource::class.java.declaredFields
        for (field in declaredFields) {
            if (field.type === Camera::class.java) {
                field.isAccessible = true
                try {
                    val camera: Camera = field.get(cameraSource) as Camera
                    if (camera != null) {
                        val params: Camera.Parameters = camera.getParameters()
                        params.flashMode = mode
                        camera.setParameters(params)

                    }

                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }

    }

    private fun getFlash(cameraSource: CameraSource?): Boolean {
        val declaredFields: Array<Field> = CameraSource::class.java.declaredFields
        for (field in declaredFields) {
            if (field.type === Camera::class.java) {
                field.isAccessible = true
                try {
                    val camera: Camera = field.get(cameraSource) as Camera
                    if (camera != null) {
                        val params: Camera.Parameters = camera.parameters
                        if (params.flashMode == FLASH_MODE_ON) {
                            return true
                        }
                    }
                    return false
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return false
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> findNavController().popBackStack()
            R.id.iv_flash -> switchFlashLight(true)
            R.id.iv_gallery -> chooseImageGallery()
            R.id.btn_my_qr -> gotToQR()
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            setBarcodeScanner()
        }
        cameraSource?.start(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        return
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource?.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetScanner()

    }

    private fun resetScanner() {
        barcodeDetector.release()
        if (cameraSource != null) {
            try {
                cameraSource?.stop()
                cameraSource?.release()
            } catch (ignored: NullPointerException) {
            }
            cameraSource = null
        }
    }


    private fun goToSendScreen(code: Barcode) {
        kotlin.runCatching {
            viewModel.setScanAdd(code.rawValue.toString())
            view?.post {
                findNavController().popBackStack()
            }
        }
    }

    private fun gotToQR() {
        val args = Bundle()
        args.putSerializable(Constants.COIN, coin)
        findNavController().navigate(R.id.my_qr, args)
    }

    override fun scanQRCallBack(code: Barcode, type: Int) {
//         super.scanQRCallBack(code, type)

        goToSendScreen(code)
    }

    fun switchFlashLight(status: Boolean) {
        try {
            mCameraId?.let { mCameraManager?.setTorchMode(it, status) }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) + ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                (activity as HomeActivity?)?.recreate()
            } else {
                makeRequest()
            }
        } else {
            setBarcodeScanner()
        }
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")

                }
            }
        }
    }
}