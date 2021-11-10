package io.tux.wallet.testnet.viewModels

import android.app.Application
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * View model for interacting with CameraX.
 * Create an instance which interacts with the camera service via the given application context.
 */
class CameraXViewModel(application: Application) : AndroidViewModel(application) {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

//    // Handle any errors (including cancellation) here.
//    val processCameraProvider: LiveData<ProcessCameraProvider>
//        get() {
//            if (cameraProviderLiveData == null) {
//                cameraProviderLiveData = MutableLiveData()
//                val cameraProviderFuture =
//                    ProcessCameraProvider.getInstance(getApplication())
//                cameraProviderFuture.addListener(
//                    Runnable {
//                        try {
//                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
//                        } catch (e: ExecutionException) {
//                            // Handle any errors (including cancellation) here.
//                            Log.e(TAG, "Unhandled exception", e)
//                        } catch (e: InterruptedException) {
//                            Log.e(TAG, "Unhandled exception", e)
//                        }
//                    },
//                    ContextCompat.getMainExecutor(getApplication())
//                )
//            }
//            return cameraProviderLiveData!!
//        }

    companion object {
        private const val TAG = "CameraXViewModel"
    }
}
