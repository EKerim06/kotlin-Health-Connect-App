package com.example.healt_connect_test_app.app_constants

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

const val healthConnectAppName = "com.google.android.apps.healthdata"

const val connectAppUrl =
    "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=tr_TR"

fun showAlertDialog(
    message: String,
    positiveButtonClickListener: DialogInterface.OnClickListener,
    negativeButtonOnClickListener: DialogInterface.OnClickListener,
    builder: AlertDialog.Builder,
) {
    builder.setMessage(message)

    builder.setPositiveButton("Evet", positiveButtonClickListener)

    builder.setNegativeButton("Hayir", negativeButtonOnClickListener)

    builder.create().show()
}