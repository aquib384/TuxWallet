package io.tux.wallet.testnet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.FragmentChangePinBinding
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.utils.Validations

class AddBankAccountFragment : Fragment(), View.OnClickListener {
  lateinit var binding :FragmentChangePinBinding
    private var isValidate = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.etOldPassword.doOnTextChanged{ text, _, _, _ ->
            val isPassValidate: Boolean = Validations.pinValidate(text.toString(), binding.layOldPassword)
            if (!isPassValidate)
            {
                binding.layOldPassword.error = null
                binding.layOldPassword.isErrorEnabled = false
                isValidate = true

            }

        }
        binding.etPassword.doOnTextChanged{ text, _, _, _ ->
            val isPassValidate: Boolean = Validations.pinValidate(text.toString(), binding.layPassword)
            if (!isPassValidate)
            {
                binding.layPassword.error = null
                binding.layPassword.isErrorEnabled = false
                isValidate = true

            }

        }
        binding.etCpassword.doOnTextChanged{ text, _, _, _ ->
            val isPassValidate: Boolean = Validations.pinValidate(text.toString(), binding.layCpassword)
            if (!isPassValidate)
            {
                binding.layCpassword.error = null
                binding.layCpassword.isErrorEnabled = false
                isValidate = true

            }

        }
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_confirm->validate(binding.etOldPassword.text.toString(),binding.etPassword.text.toString(),binding.etCpassword.text.toString())
        }
    }



    private fun validate(oldpass :String ,pass: String, cpass: String){
        when {

            oldpass.isEmpty() -> {
                isValidate = false
                binding.layOldPassword.error = resources.getString(R.string.pin_error)
            }
            pass.isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.pin_error)
            }
            cpass.isEmpty() ->
            {
                isValidate = false
                binding.layCpassword.error = resources.getString(R.string.cpin_error)
            }
            else -> {
                Utils.showToast(requireContext(),"Updated Successfully")
                findNavController().popBackStack()
            }
        }



    }

}