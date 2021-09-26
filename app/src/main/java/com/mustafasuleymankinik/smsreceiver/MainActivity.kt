package com.mustafasuleymankinik.smsreceiver

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.mustafasuleymankinik.smsreceiver.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG: String = MainActivity.javaClass.name
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var smsBroadCastReceiver: SmsBroadCastReceiver
    private var intentFilter: IntentFilter? = null
    private lateinit var client:SmsRetrieverClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btReceiveSms.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            initSmsRetriever()
            initBroadCastReceiver()
        }

    }
    private fun initSmsRetriever() {

        client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener(object : OnSuccessListener<Void> {
            override fun onSuccess(p0: Void?) {
                Log.d(TAG, "onSuccess:$p0")
            }
        })
        task.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                p0.printStackTrace()
            }
        })
    }

    private fun initBroadCastReceiver() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsBroadCastReceiver = SmsBroadCastReceiver()
        smsBroadCastReceiver.setListener(object : SmsListener{
            override fun smsCode(sms: String) {
                takeCode(sms)
            }
        })
        registerReceiver(smsBroadCastReceiver,intentFilter)
    }

    private fun takeCode(sms:String) {
        binding.tvSms.text = sms.substring(53,57)
        binding.progressBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadCastReceiver)
    }
}