package com.mustafasuleymankinik.smsreceiver

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.mustafasuleymankinik.smsreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG: String = MainActivity.javaClass.name
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var smsBroadCastReceiver: SmsBroadCastReceiver
    private var intentFilter: IntentFilter? = null
    private lateinit var client:SmsRetrieverClient
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            ,ActivityResultCallback{result ->
                if (result.resultCode == Activity.RESULT_OK)
                {
                    result.data?.let {
                        binding.tvSms.text = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.substring(7,11)
                        binding.progressBar.visibility = View.GONE
                        binding.ivDone.visibility = View.VISIBLE
                    }
                }
            })

    }

    override fun onResume() {
        super.onResume()
        binding.btReceiveSms.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            initSmsRetriever()
            initBroadcastReceiver()
        }
    }
    
    private fun initSmsRetriever() {

        client = SmsRetriever.getClient(this)
        val task = client.startSmsUserConsent(null)
    }

    private fun initBroadcastReceiver() {
        smsBroadCastReceiver = SmsBroadCastReceiver()
        smsBroadCastReceiver.setListener(object : SmsListener{
            override fun getIntent(intent: Intent?) {
                intent?.let {

                    launcher.launch(it)
                }
            }
        })
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadCastReceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadCastReceiver)
    }
}