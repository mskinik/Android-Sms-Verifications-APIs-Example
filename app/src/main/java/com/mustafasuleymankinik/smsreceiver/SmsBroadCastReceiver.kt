package com.mustafasuleymankinik.smsreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * Created by mustafasuleymankinik on 25.09.2021.
 */
class SmsBroadCastReceiver: BroadcastReceiver() {
    lateinit var smsListener1: SmsListener

    fun setListener(smsListener: SmsListener){
//        Log.d("TAG", "setListener: girdi6 check${this.smsListener != null} ")
        Log.d("TAG", "setListener: girdi6 ${smsListener}")
        this.smsListener1 = smsListener
            Log.d("TAG", "setListener: girdi6 check${this.smsListener1 != null} ")
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("TAG", "onReceive: girdi6")
        if(SmsRetriever.SMS_RETRIEVED_ACTION == p1?.action)
        {
            val bundle = p1.extras
            val status= bundle?.get(SmsRetriever.EXTRA_STATUS) as Status
            when(status.statusCode)
            {

                CommonStatusCodes.SUCCESS -> {
                    val message = bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE)
                    Log.d("TAG", "onReceive: girdi6 succes ${message}")
                    smsListener1.smsCode(message as String)
                    //smsListener.smsCode("message as String")
                }
                CommonStatusCodes.TIMEOUT -> print("")
                else -> print("")
            }
        }
    }
}