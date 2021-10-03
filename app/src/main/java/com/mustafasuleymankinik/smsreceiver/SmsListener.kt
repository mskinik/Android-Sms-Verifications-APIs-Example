package com.mustafasuleymankinik.smsreceiver

import android.content.Intent

/**
 * Created by mustafasuleymankinik on 26.09.2021.
 */
interface SmsListener {
    fun getIntent(intent: Intent?)
}