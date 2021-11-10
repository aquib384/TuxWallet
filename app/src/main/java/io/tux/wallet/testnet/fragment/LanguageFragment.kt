package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.HomeActivity

import io.tux.wallet.testnet.databinding.FragmentLanguageBinding
import io.tux.wallet.testnet.utils.LocaleHelper
import io.tux.wallet.testnet.utils.Utils


class LanguageFragment : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    lateinit var binding :FragmentLanguageBinding
    private var lang :String = ""
    private var langIndex :Int = 0
    var from :String = ""

lateinit var sharedPref:SharedPref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageBinding.inflate(layoutInflater)
        sharedPref = SharedPref(requireContext())
        binding.ivBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.cardEnglish.setOnClickListener(this)
        binding.cardChinese.setOnClickListener(this)
        binding.cardJapanese.setOnClickListener(this)
        binding.radioJapanese.setOnCheckedChangeListener(this)
        binding.radioEnglish.setOnCheckedChangeListener(this)
        binding.radioChinese.setOnCheckedChangeListener(this)

        when (sharedPref.getLanguage())
        {
            0-> selectButton(binding.radioEnglish, binding.radioChinese, binding.radioJapanese)
           1-> selectButton(binding.radioJapanese, binding.radioChinese, binding.radioEnglish)
           2-> selectButton(binding.radioChinese,binding.radioJapanese,binding.radioEnglish)
        }
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {

            R.id.iv_back -> findNavController().popBackStack()
            R.id.card_english -> {
                selectButton(binding.radioEnglish, binding.radioChinese, binding.radioJapanese)
                lang = "en"
                langIndex=0
//                Utils.showToast(this, lang)
            }

            R.id.card_japanese -> {
                selectButton(binding.radioJapanese, binding.radioChinese, binding.radioEnglish)
                lang = "ja"
                langIndex=1
//                Utils.showToast(this, lang)
            }
            R.id.card_chinese ->
            {
                selectButton(binding.radioChinese,binding.radioJapanese,binding.radioEnglish)
                lang = "zh"
                langIndex=2
//                Utils.showToast(this, lang)
            }
            R.id.btn_confirm ->
            {
                if (lang.isNotEmpty()) {
                   sharedPref.setLanguage(langIndex)
                    LocaleHelper.setLocale(requireContext(), lang)
                    Utils.setAppLocale(requireContext(),lang)
                    findNavController().popBackStack()
                    (activity as HomeActivity?)?.recreate()
                }
                else
                {
                    Utils.showToast(requireContext(),"Select a language to continue")
                }
            }

        }

    }


    private fun selectButton(b1 : RadioButton, b2 : RadioButton, b3 : RadioButton)
    {
        b1.isChecked = true
        b2.isChecked = false
        b3.isChecked = false

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id)
        {
            R.id.radio_english -> if (isChecked){ lang = "en"
                selectButton(binding.radioEnglish, binding.radioChinese, binding.radioJapanese)

            }
            R.id.radio_chinese ->   if (isChecked){
                lang = "zh"
                selectButton(binding.radioChinese, binding.radioEnglish, binding.radioJapanese)}
            R.id.radio_japanese ->   if (isChecked){
                lang = "ja"
                selectButton(binding.radioJapanese, binding.radioEnglish, binding.radioChinese)}
        }


    }
}