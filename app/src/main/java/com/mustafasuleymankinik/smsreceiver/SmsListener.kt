package com.mustafasuleymankinik.smsreceiver

/**
 * Created by mustafasuleymankinik on 26.09.2021.
 */
interface SmsListener {
    fun smsCode(sms:String)
}