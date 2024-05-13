package com.isbank.isim.ui.activity.stepcountactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healt_connect_test_app.R
//import androidx.health.connect.client.HealthConnectClient
import java.lang.Exception

// TODO not used

class StepCountActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        builder = AlertDialog.Builder(this)

//        checkHealthConnectApp()


    }

//    private fun checkHealthConnectApp() {
//        if (controlApp()) {
//
//            val connectAppStatus = HealthConnectClient.getSdkStatus(this, healthConnectAppName)
//
//            if (connectAppStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED || connectAppStatus == HealthConnectClient.SDK_UNAVAILABLE) {
//
//                showAlertDialog(
//                    "Health connect uygulamasini guncelleyiniz!",
//                    { _, _ -> openGooglePlay() },
//                    { _, _ -> closeApp() },
//                    builder,
//                )
//
//            } else {
//
////                val nextPage = Intent(this, PersonalDatas::class.java)
////                startActivity(nextPage)
//                Toast.makeText(this@StepCountActivity, "Uygulama Calisiyor", Toast.LENGTH_LONG)
//                    .show()
//
//            }
//
//        } else {
//            showAlertDialog(
//                "Lutfen Health connect uygulamasini indirin",
//                { _, _ -> openGooglePlay() },
//                { _, _ -> closeApp() },
//                builder,
//            )
//        }
//    }
//
//    private fun closeApp() {
//        finishAffinity()
//        Toast.makeText(this@StepCountActivity, "Uygulama Kapatiliyor", Toast.LENGTH_LONG).show()
//    }
//
//    private fun openGooglePlay() {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(connectAppUrl))
//        startActivity(intent)
//    }
//
//    private fun controlApp(): Boolean {
//        return try {
//            this@StepCountActivity.packageManager.getApplicationInfo(healthConnectAppName, 0)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }

}

//package com.isbank.isim.ui.activity.stepcountactivity
//
//import android.content.DialogInterface
//import androidx.appcompat.app.AlertDialog
//
//const val healthConnectAppName = "com.google.android.apps.healthdata"
//
//const val connectAppUrl =
//    "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=tr_TR"
//
//fun showAlertDialog(
//    message: String,
//    positiveButtonClickListener: DialogInterface.OnClickListener,
//    negativeButtonOnClickListener: DialogInterface.OnClickListener,
//    builder: AlertDialog.Builder,
//) {
//    builder.setMessage(message)
//
//    builder.setPositiveButton("Evet", positiveButtonClickListener)
//
//    builder.setNegativeButton("Hayir", negativeButtonOnClickListener)
//
//    builder.create().show()
//}

//<activity
//android:name=".ui.activity.stepcountactivity.StepCountActivity"
//android:exported="true"
//android:screenOrientation="portrait"
//android:theme="@style/AppTheme.NoActionBar">
//
//<intent-filter>
//<action android:name="android.intent.action.MAIN" />
//<category android:name="android.intent.category.LAUNCHER" />
//<!--                <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />-->
//
//</intent-filter>
//</activity>
//
//<uses-permission android:name="android.permission.health.READ_STEPS" />
//
//<queries>
//<package android:name="com.google.android.apps.healthdata" />
//</queries>
