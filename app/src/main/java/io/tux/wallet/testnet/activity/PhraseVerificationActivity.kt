package io.tux.wallet.testnet.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.AnswerOptionsKeyAdapter
import io.tux.wallet.testnet.databinding.ActivityPhraseVerificationBinding
import io.tux.wallet.testnet.databinding.ItemAnswerBinding
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PhraseVerificationActivity : AppCompatActivity(), AnswerOptionsKeyAdapter.AnswerInterface,
    View.OnClickListener {
    lateinit var binding : ActivityPhraseVerificationBinding
    private lateinit var optionsAdapter : AnswerOptionsKeyAdapter
    private var isFirst = true
    private var ans =""
    private lateinit var optionList :MutableList<String>
    var list =ArrayList<String>()
    private var keyList =ArrayList<String>()
    private var randomNumber: Int =0
    private var prevRandomNumber: Int =0
    lateinit var sharedPref :SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhraseVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref= SharedPref(this)
        keyList = intent.getStringArrayListExtra(KEYLIST) as ArrayList<String>
        optionList = mutableListOf()
        setQuestions()

        binding.ivBack.setOnClickListener(this)
    }

    override fun onOptionSelectedCallBack(position: Int,binding: ItemAnswerBinding) {

        if (optionList[position] == ans) {
            binding.tvNo.setTextColor(resources.getColor(R.color.green,resources.newTheme()))
            if (isFirst) {
                Timer(resources.getString(R.string.word_phrase), false).schedule(300) {
                    runOnUiThread {
                        isFirst = false
                        setQuestions()
                    }
                }
            }
            else {
                val i = Intent(this, SetPinActivity::class.java)
                i.putStringArrayListExtra(KEYLIST,keyList)
                startActivity(i)
                finish()
                finishAndRemoveTask()
            }
        } else {
            binding.tvNo.setTextColor(resources.getColor(R.color.red,resources.newTheme()))
            Timer(resources.getString(R.string.word_phrase), false).schedule(300) {
                runOnUiThread {
                    finish()
                }
            }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setQuestions()
    {
        try {
            optionList.clear()
            list.clear()
            randomNumber = Utils.getRandomNumber(1,12)

            if (prevRandomNumber== randomNumber)
            {
                randomNumber = Utils.getRandomNumber(1,12)
            }
            prevRandomNumber = randomNumber
            Log.e("random", "$randomNumber")
            val c = getNumberFormat(randomNumber)
            Log.e("char",c)
            binding.tvQuestion.text = resources.getString(R.string.select)+" " +c +" "+resources.getString(R.string.the_word)
                ans = keyList[randomNumber - 1]
            Log.e("ans",ans)
               list.add(ans)
         for(i in 0..keyList.size) {
             Log.e("optionssss", list.toString())
             if (list.size ==6) {
                 Log.e("optionsList", list.toString())
                 optionList= list.shuffled() as MutableList<String>
                 Log.e("shuffeled",optionList.toString())
                 optionsAdapter = AnswerOptionsKeyAdapter(this,optionList,this)
                 binding.rvAns.apply {
                     layoutManager = GridLayoutManager(context,3)
                     adapter = optionsAdapter
                 }
                 break
             }
             else
             {
                 val r = Utils.getRandomNumber(1, 12)
                 Log.e("nesxt r", r.toString())
                 if (!list.contains(keyList[r - 1])) {
                     list.add(keyList[r - 1])
                 }
             }
         }

        }
        catch (e:Exception)
        {e.printStackTrace()}
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_back-> finish()
        }
    }


    private fun getNumberFormat(num: Int): String {
        var s =""
        try {
            s= when (num) {
                1 -> "$num st"
                2 -> "$num nd"
                3 -> "$num rd"
                else -> "$num th"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return s
    }


    }
