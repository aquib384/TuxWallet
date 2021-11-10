package io.tux.wallet.testnet.utils

import android.text.TextUtils
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

import java.util.regex.Matcher
import java.util.regex.Pattern


object Validations {

    fun checkEmailValidates(username: String, emailTextInputLayout: TextInputLayout): Boolean {
        var isError = false
        val emailPattern = Patterns.EMAIL_ADDRESS
        if (username.isEmpty()) {
            isError = true
            emailTextInputLayout.error = "Please enter email"
        } else if (!username.matches(emailPattern.toRegex())) {
            isError = true
            emailTextInputLayout.error = "Please enter a valid email address"
        }
        return isError
    }
    fun checkMobileValidates(username: String, emailTextInputLayout: TextInputLayout): Boolean {
        var isError = false
        val emailPattern = Patterns.PHONE
        if (username.isEmpty()) {
            isError = true
            emailTextInputLayout.error = "Please enter mobile number"
        } else if (!username.matches(emailPattern.toRegex())) {
            isError = true
            emailTextInputLayout.error = "Please enter a valid mobile number"
        }
        return isError
    }

    fun checkUsernameValidates(
        username: String,
        textInputLayout: TextInputLayout
    ): Boolean {
        val pattern = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$"
        var isError = false
        if (username.isEmpty()) {
            isError = true
            textInputLayout.error = "Please enter Full Name"
        }
        else if (!username.matches(pattern.toRegex())) {
            isError = true
         textInputLayout.error = "Please enter a valid Name"
        }
        return isError
    }

    fun checkPasswordValidates(
        password: String,
        passwordTextInputLayout: TextInputLayout
    ): Boolean {
        var isError = false
        if (password.isEmpty()) {
            isError = true
            passwordTextInputLayout.error = "Please enter password"
        } else if (!passwordValidate(password, passwordTextInputLayout)) {
            isError = true
        }
        return isError
    }
  fun pinValidate(
        pass: String,
        passwordTextInputLayout: TextInputLayout
    ): Boolean {
        if (pass.isEmpty()) {
            passwordTextInputLayout.error = "Please enter password"
            return false
        } else if (pass.length < 6) {
            passwordTextInputLayout.error = "Pin must have 6 digits"
            return false
        }
      return true

    }


        // if you want to know which requirement was not met
    private fun passwordValidate(
        pass: String,
        passwordTextInputLayout: TextInputLayout
    ): Boolean {
        if (pass.length < 8) {
            passwordTextInputLayout.error = "Password must have at least 8 characters"
            return false
        }

        if (!pass.matches(".*\\d.*".toRegex())) {
            passwordTextInputLayout.error = "Password must include one number"
            return false
        }
        if (!pass.matches(".*[a-z].*".toRegex())) {
            passwordTextInputLayout.error = "Password must include one lowercase letter"
            return false
        }
        if (!pass.matches(".*[A-Z].*".toRegex())) {
            passwordTextInputLayout.error = "Password must include one uppercase letter"
            return false
        }
        if (!pass.matches(".*[!@#$%^&*+=?\"'(),-./:;<>_`{|}~-].*".toRegex())) {
            passwordTextInputLayout.error =
                "Password must include one special character !@#\$%^&*+=?\"'(),-./:;<>_`{|}~"
            return false
        }
//        if (containsPartOf(pass, email)) {
//            passwordTextInputLayout.error = "Password contains username, please make it unique"
//            return false
//        }
        return true
    }

    fun urlValidations(
        facebookUrl: String?,
        instagramUrl: String?,
        twitterUrl: String?,
        facebook: EditText,
        twitter: EditText,
        instagram: EditText,
        msg:String
    ): Boolean {
        var validate = true
        facebookUrl?.let {
            if (it.isNotEmpty()) {
                if (!URLUtil.isValidUrl(it)) {
                    validate = false
                    facebook.error = msg
                }
            }
        }

        instagramUrl?.let {
            if (it.isNotEmpty()) {
                if (!URLUtil.isValidUrl(it)) {
                    validate = false
                    instagram.error = msg
                }
            }
        }

        twitterUrl?.let {
            if (it.isNotEmpty()) {
                if (!URLUtil.isValidUrl(it)) {
                    validate = false
                    twitter.error = msg
                }
            }
        }
        return validate

    }

    /*fun containsPartOf(pass: String, username: String): Boolean {
        val requiredMin = 4
        var i = 0
        while (i + requiredMin < username.length) {
            if (pass.contains(username.substring(i, i + requiredMin))) {
                return true
            }
            i++
        }
        return false
    }*/

   fun isValidPhoneNumber( phoneNumber :CharSequence):Boolean {
//     const  phoneRegExp = /^((\\+[1-9]{1,4}[ \\-]*)|(\\([0-9]{2,3}\\)[ \\-]*)|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?$/
       val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"
       val pattern: Pattern = Pattern.compile(regex)
       if (!TextUtils.isEmpty(phoneNumber)) {
               val matcher: Matcher = pattern.matcher(phoneNumber)
            return matcher.matches()
        }
        return false;
    }


//    fun isValidMobileNumber(context: Context,phone: String?): Boolean {
//        if (TextUtils.isEmpty(phone)) return false
//        val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)
//        try {
//            val phoneNumber = phoneNumberUtil.parse(phone, Locale.getDefault().getCountry())
//            val phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber)
//            return phoneNumberType == PhoneNumberType.MOBILE
//        } catch (e: Exception) {
//        }
//        return false
//    }
//
//    fun validatePhoneNumberWithCountry(context: Context, phNumber: String): Boolean {
//        val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)
////        Log.e("phone","$phNumber")
////        var phNo = phNumber.split(" ")
////        Log.e("phonesss",""+phNo[0])
////        var countryCode = getCountryModel(context,phNo[0])
////        Log.e("countryModel","${countryCode.code}--- ${countryCode.country_code} +-- ${countryCode.name} " )
////        val isoCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode.country_code.toInt())
////        Log.e("country ","$phNo --- ${countryCode.country_code} +-- $isoCode " )
//
//        var phoneNumber: Phonenumber.PhoneNumber? = null
//
//        try {
//            for (region in phoneNumberUtil.supportedRegions) {
//                //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
//                phoneNumber = phoneNumberUtil.parse(phNumber, region)
//
//                val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
//                return if (isValid) {
//                    val internationalFormat =
//                        phoneNumberUtil.format(
//                            phoneNumber,
//                            PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
//                        )
////                    Utils.showToast(context, "Phone Number is Valid $internationalFormat")
//                    true
//                } else {
////                    Utils.showToast(context, "Phone Number is Invalid $phoneNumber")
//
//                    false
//                }
//
//            }
//        }catch (e: NumberParseException) {
//            e.printStackTrace()
//        }
//        return false
//    }




}
