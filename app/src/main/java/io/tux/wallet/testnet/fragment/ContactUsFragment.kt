package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.FragmentContactUsBinding
import io.tux.wallet.testnet.utils.Constants.DESCRIPTION
import io.tux.wallet.testnet.utils.Constants.EMAILID
import io.tux.wallet.testnet.utils.Constants.FULLNAME
import io.tux.wallet.testnet.utils.Constants.SUBJECT
import io.tux.wallet.testnet.utils.Validations
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel


class ContactUsFragment : Fragment(), View.OnClickListener {
   lateinit var binding :FragmentContactUsBinding
    private var isValidate = false
    private val viewModel: OnBoardingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
        binding.tvInstruction.text= Html.fromHtml(resources.getString(R.string.instructions))

        binding.etName.doOnTextChanged{ text, _, _, _ ->

            if (!text.isNullOrEmpty())
            {
                binding.layName.error = null
                binding.layName.isErrorEnabled = false
                isValidate = true

            }
            else{
                isValidate = false
            }

        }
        binding.etSubject.doOnTextChanged{ text, _, _, _ ->

            if (!text.isNullOrEmpty())
            {
                binding.laySubject.error = null
                binding.laySubject.isErrorEnabled = false
                isValidate = true

            }
            else{
                isValidate = false
            }

        }
        binding.etDesc.doOnTextChanged{ text, _, _, _ ->

            if (!text.isNullOrEmpty())
            {
                binding.layDesc.error = null
                binding.layDesc.isErrorEnabled = false
                isValidate = true

            }
            else{
                isValidate = false
            }

        }
        binding.etEmail.doOnTextChanged{ text, _, _, _ ->
            val isEmailValidate: Boolean = Validations.checkEmailValidates(text.toString(), binding.layEmail)
            if (!isEmailValidate)
            {
                binding.layEmail.error = null
                binding.layEmail.isErrorEnabled = false
                isValidate = true

            }
            else
            {
                isValidate = false
            }

        }
        return binding.root
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_send -> validate()
        }
    }

    private fun validate() {

        when {
            binding.etName.text.toString().isEmpty() -> {
                isValidate = false
                binding.layName.error = resources.getString(R.string.name_error)
            }

            binding.etEmail.text.toString().isEmpty() -> {
                isValidate = false
                binding.layEmail.error = resources.getString(R.string.email_error)
            }
            binding.etSubject.text.toString().isEmpty() -> {
                isValidate = false
                binding.laySubject.error = resources.getString(R.string.subject_error)
            }
            binding.etDesc.text.toString().isEmpty() -> {
                isValidate = false
                binding.layDesc.error = resources.getString(R.string.desc_error)
            }



            else ->
            {
                if (isValidate) {
                    binding.progressBar.visibility = VISIBLE
                    viewModel.sendEmail(hashMapOf(
                        FULLNAME to   binding.etName.text.toString().trim(),
                        SUBJECT  to   binding.etSubject.text.toString().trim(),
                        EMAILID  to   binding.etEmail.text.toString().trim(),
                        DESCRIPTION  to   binding.etDesc.text.toString().trim()
                    ))

                    viewModel.contactUsModel.observe(viewLifecycleOwner)
                    {
                        Log.e("contactUs",it.toString())
                        binding.progressBar.visibility = GONE
                        if(it.code==200) {
                            findNavController().navigate(R.id.email_verify)
                        }
                        else
                        {

                        }
                    }

//
                }
            }

        }
    }
}