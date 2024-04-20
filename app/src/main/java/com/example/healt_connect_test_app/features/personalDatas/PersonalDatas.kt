package com.example.healt_connect_test_app.features.personalDatas

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healt_connect_test_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

val dataPermission =
    setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getWritePermission(SpeedRecord::class),
    )

class PersonalDatas : AppCompatActivity() {

    private lateinit var healthConnectClient: HealthConnectClient

    private lateinit var stepCountTextView: TextView

    private lateinit var speedTextView: TextView

    private lateinit var heartRateTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_datas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        healthConnectClient = HealthConnectClient.getOrCreate(this)

        stepCountTextView = findViewById(R.id.stepsTextView)

        speedTextView = findViewById(R.id.speedTextView)

        heartRateTextView = findViewById(R.id.heartCountView)


        CoroutineScope(Dispatchers.Main).launch {
            checkPermissionAndRun()
        }

    }

    private val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()

    private val requestPermission =
        registerForActivityResult(requestPermissionActivityContract) { granted ->

            if (granted.containsAll(dataPermission)) {
                // all permission granted
                Toast.makeText(this, "Tum izinler verildi", Toast.LENGTH_LONG).show()
            } else {
                // show toast message
                Toast.makeText(this, "bazı izinler reddedildi", Toast.LENGTH_LONG).show()
            }

        }

    private suspend fun checkPermissionAndRun() {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()

        if (granted.containsAll(dataPermission)) {
            // permission al ready granted read or write data
            val time = LocalDateTime.now()
            val daysAgo = time.minusDays(10)
            val instant = daysAgo.toInstant(ZoneOffset.UTC)
            aggregateSteps(
                healthConnectClient,
                startTime = instant,
                endTime = Instant.now()
            )


            aggregateHeart(
                healthConnectClient,
                startTime = instant,
                endTime = Instant.now(),
            )

            aggregateSpeed(
                healthConnectClient,
                startTime = instant,
                endTime = Instant.now(),
            )

        } else {
            requestPermission.launch(dataPermission)
        }

    }

    private suspend fun aggregateSteps(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[StepsRecord.COUNT_TOTAL]
            println("step count read value : $stepCount")
            stepCountTextView.text = (stepCount ?: "N/a").toString()
        } catch (e: Exception) {
            // Run error handling here
            println("=====================  aggregateSteps error : $e ")

        }
    }

    private suspend fun aggregateHeart(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(HeartRateRecord.BPM_AVG),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[HeartRateRecord.BPM_AVG]
            println("step count read value : $stepCount")
            heartRateTextView.text = (stepCount ?: "N/a").toString()
        } catch (e: Exception) {
            // Run error handling here
            println("=====================  aggregateHeart error : $e ")

        }
    }

    private suspend fun aggregateSpeed(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(SpeedRecord.SPEED_AVG),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[SpeedRecord.SPEED_AVG]
            println("step count read value : $stepCount")
            speedTextView.text = (stepCount ?: "N/a").toString()
        } catch (e: Exception) {
            // Run error handling here
            println("=====================  aggregateSpeed error : $e ")
        }
    }

}