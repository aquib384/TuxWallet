package io.tux.wallet.testnet.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.viewpagers.WelcomePagerAdapter
import io.tux.wallet.testnet.databinding.ActivityWelcomeBinding
import io.tux.wallet.testnet.model.WelcomePagerModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.LocaleHelper
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import wallet.core.jni.HDWallet
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    private var selectedLanguaPosition: Int? = 0
    private var checkedItem: Int = 0
    lateinit var binding: ActivityWelcomeBinding
    var pagesList = ArrayList<WelcomePagerModel>()

    var langList = ArrayList<String>()

    @Inject
    lateinit var sharedPref: SharedPref

    var lang: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.setDefaultCurrency(this, sharedPref)
        setUpSpinner()
        setPagerAdapter()
        binding.btnLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
        binding.tvGuest.setOnClickListener(this)


    }

    private fun setPagerAdapter() {

        pagesList.add(
            WelcomePagerModel(
                R.drawable.ic_screen1,
                resources.getString(R.string.screen1_title),
                resources.getString(R.string.screen1_desc)
            )
        )
        pagesList.add(
            WelcomePagerModel(
                R.drawable.ic_screen2,
                resources.getString(R.string.screen2_title),
                resources.getString(R.string.screen2_desc)
            )
        )
//        pagesList.add(WelcomePagerModel(R.drawable.ic_screen3,  resources.getString(R.string.screen3_title),resources.getString(R.string.screen3_desc)))
        binding.viewpager.apply {
            adapter = WelcomePagerAdapter(context, pagesList)

        }

        TabLayoutMediator(binding.intoTabLayout, binding.viewpager)
        { tab, position -> }.attach()
    }


    private fun setUpSpinner() {
        langList.add(resources.getString(R.string.english))
        langList.add(resources.getString(R.string.japanese))
        langList.add(resources.getString(R.string.chinese))

        if (sharedPref.getLanguage()!=null) {

            binding.language.text = when (sharedPref.getLanguage()) {
               1 -> {
                    checkedItem = 1
                    resources.getString(R.string.japanese)
                }
                2 -> {
                    checkedItem = 2
                    resources.getString(R.string.chinese)
                }
                else -> {
                    checkedItem = 0
                    resources.getString(R.string.english)
                }
            }
        }
        else
        {
            checkedItem = 0
            binding.language.text= resources.getString(R.string.english)
            lang = "en"
            sharedPref.setLanguage(checkedItem)

        }
        binding.language.setOnClickListener {
            showDialogs()
        }
        /*  binding.spinnerLang.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
              override fun onItemSelected(
                  parent: AdapterView<*>?,
                  view: View?,
                  position: Int,
                  id: Long
              ) {
                  lang = when (langList[position]) {
                      resources.getString(R.string.english) -> "en"
                      resources.getString(R.string.japanese) -> "ja"
                      else -> "zh"
                  }
                  if (lang.isNotEmpty()) {
                      sharedPref.setLanguage(lang)
                      LocaleHelper.setLocale(this@WelcomeActivity, lang)
                      Utils.setAppLocale(this@WelcomeActivity, lang)
                  }
              }

              override fun onNothingSelected(parent: AdapterView<*>?) {
                  TODO("Not yet implemented")
              }
          })*/
    }

    private fun showDialogs() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.please_select_language))
            setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                lang ="en"
            }
            setPositiveButton(resources.getString(R.string.select)) { _, _ ->
                lang = when (langList[selectedLanguaPosition!!]) {
                    resources.getString(R.string.english) -> "en"
                    resources.getString(R.string.japanese) -> "ja"
                    else -> "zh"
                }
                Log.e("welcome_lang",lang)
                if (lang.isNotEmpty()) {
                    sharedPref.setLanguage(selectedLanguaPosition!!)
                    Log.e("sharedPref_lang",sharedPref.getLanguage().toString())
                    LocaleHelper.setLocale(this@WelcomeActivity, lang)
                    Utils.setAppLocale(this@WelcomeActivity, lang)
                    recreate()
                }
            }
            setSingleChoiceItems(langList.toTypedArray(), checkedItem) { dialog, which ->
                selectedLanguaPosition = which
            }
        }.show()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_signup -> startActivity(
                Intent(
                    this,
                    io.tux.wallet.testnet.activity.CreateWalletActivity::class.java
                )
            )
            R.id.btn_login -> startActivity(Intent(this, RecoverWalletActivity::class.java))
            R.id.tv_guest -> {
                Log.e("lang_guest",sharedPref.getLanguage().toString())
                sharedPref.setPhrase(HDWallet(sharedPref.getPhrase(), "").mnemonic())
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }





}
