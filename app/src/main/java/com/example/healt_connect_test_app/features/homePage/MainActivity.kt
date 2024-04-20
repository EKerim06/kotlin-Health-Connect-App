package com.example.healt_connect_test_app.features.homePage

import android.content.DialogInterface
import android.content.Intent
import android.health.connect.HealthConnectManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.health.connect.client.HealthConnectClient
import com.example.healt_connect_test_app.R
import com.example.healt_connect_test_app.app_constants.connectAppUrl
import com.example.healt_connect_test_app.app_constants.healthConnectAppName
import com.example.healt_connect_test_app.app_constants.showAlertDialog
import com.example.healt_connect_test_app.features.personalDatas.PersonalDatas
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var connect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connect = findViewById(R.id.health_connect)

        val alertDialogBuilder = AlertDialog.Builder(this)


        connect.setOnClickListener {
            if (controlApp()) {

                val connectAppStatus = HealthConnectClient.getSdkStatus(this, healthConnectAppName)

                if (connectAppStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED || connectAppStatus == HealthConnectClient.SDK_UNAVAILABLE) {

                    showAlertDialog(
                        getString(R.string.home_page_app_update_available),
                        { _, _ -> openGooglePlay() },
                        { _, _ -> closeApp() },
                        alertDialogBuilder,
                    )

                } else {
                    val nextPage = Intent(this, PersonalDatas::class.java)
                    startActivity(nextPage)
                }

            } else {
                showAlertDialog(
                    getString(R.string.home_page_app_control),
                    { _, _ -> openGooglePlay() },
                    { _, _ -> closeApp() },
                    alertDialogBuilder,
                )
            }
        }
    }


    private fun closeApp() {
        finishAffinity()
        Toast.makeText(this@MainActivity, "Uygulama Kapatiliyor", Toast.LENGTH_LONG).show()
    }

    private fun openGooglePlay() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(connectAppUrl))
        startActivity(intent)
    }

    private fun controlApp(): Boolean {

        return try {
            this@MainActivity.packageManager.getApplicationInfo(healthConnectAppName, 0)
            true
        } catch (e: Exception) {
            false
        }

    }

}