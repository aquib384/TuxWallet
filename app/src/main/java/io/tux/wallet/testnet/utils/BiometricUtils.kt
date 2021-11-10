package io.tux.wallet.testnet.utils

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

class BiometricUtils (var context: Context){
//    var  biometricPrompInstance: BiometricPrompt = instanceOfBiometricPrompt()
        val isBiometricPromptEnabled: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        

        /*
           * Condition I: Check if the android version in device is greater than
           * Marshmallow, since fingerprint authentication is only supported
           * from Android 6.0.
           * Note: If your project's minSdkversion is 23 or higher,
           * then you won't need to perform context check.
           *
           * */
        val isSdkVersionSupported: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        /*
           * Condition II: Check if the device has fingerprint sensors.
           * Note: If you marked android.hardware.fingerprint as something that
           * your app requires (android:required="true"), then you don't need
           * to perform context check.
           *
           * */
        fun isHardwareSupported(context: Context): Boolean {
            val fingerprintManager = FingerprintManagerCompat.from(context)
            return fingerprintManager.isHardwareDetected
        }

        /*
     * Condition III: Fingerprint authentication can be matched with a
     * registered fingerprint of the user. So we need to perform context check
     * in order to enable fingerprint authentication
     *
     * */
        fun isFingerprintAvailable(context: Context): Boolean {
            val fingerprintManager = FingerprintManagerCompat.from(context)
            return fingerprintManager.hasEnrolledFingerprints()
        }

        /*
     * Condition IV: Check if the permission has been added to
     * the app. context permission will be granted as soon as the user
     * installs the app on their device.
     *
     * */
        fun isPermissionGranted(): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) ==
                    PackageManager.PERMISSION_GRANTED
        }
        private fun checkBiometricEnabled():Boolean
        {
            val keyguardManager: KeyguardManager =context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isKeyguardSecure)
            {
                Utils.showToast(context,"Biometrics not enabled")
                return false
            }

            if( ActivityCompat.checkSelfPermission( context, android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED)
            {
                Utils.showToast(context,"Permission Denied")
                return false
            }
            return  if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
                true
            }
            else
                true

        }


//        private fun instanceOfBiometricPrompt(): BiometricPrompt {
//            val executor = ContextCompat.getMainExecutor(context)
//
//            val callback = object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                    Utils.showToast(context, "$errorCode :: $errString")
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    Utils.showToast(context, "Authentication failed for an unknown reason")
//                }
//
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    Utils.showToast(context, "Authentication was successful")
//                }
//            }
//
//            return BiometricPrompt(context, executor, callback)
//        }


    fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Device Authentication")
            .setDeviceCredentialAllowed(true)
            .build()
    }
    }
