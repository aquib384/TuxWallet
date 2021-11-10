package io.tux.wallet.testnet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ActivityGuestBinding

class GuestModeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityGuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_login->  startActivity(Intent(this, RecoverWalletActivity::class.java))
            R.id.btn_signup->  startActivity(Intent(this , io.tux.wallet.testnet.activity.CreateWalletActivity::class.java))
            R.id.iv_close ->{
                finish()
                finishAndRemoveTask()
            }
        }
    }
}