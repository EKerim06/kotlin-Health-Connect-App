package com.example.healt_connect_test_app.features.personalDatas

import MonthRename
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
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
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healt_connect_test_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val dataPermission =
    setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getWritePermission(SpeedRecord::class),
        // add to another permission and use
        // another variable google docs.
        // https://developer.android.com/health-and-fitness/guides/health-connect/plan/data-types?hl=tr#alpha10
    )

class PersonalDatas : AppCompatActivity() {

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

    private lateinit var selectedDate: LocalDate

    private lateinit var selectedMonth: Month

    private lateinit var healthConnectClient: HealthConnectClient

    private lateinit var stepCountTextView: TextView

    private lateinit var selectedDaysTextView: TextView

    private lateinit var selectedMonthTextView: TextView

    private lateinit var nextDateButton: Button

    private lateinit var lastDateButton: Button

    private lateinit var nextMonthButton: Button

    private lateinit var lastMonthButton: Button

    private lateinit var totalStepCountButton: Button

    private lateinit var listView: ListView

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

        nextDateButton = findViewById(R.id.nextDate)
        lastDateButton = findViewById(R.id.lastDate)
        nextMonthButton = findViewById(R.id.nextMonth)
        lastMonthButton = findViewById(R.id.lastMonth)

        totalStepCountButton = findViewById(R.id.total_count_button)

        selectedMonthTextView = findViewById(R.id.selectedMonthTextView)

        listView = findViewById(R.id.dalyData)
        selectedDate = LocalDate.now()

        selectedDaysTextView = findViewById(R.id.selectedDate)

        selectedDaysTextView.text = formatDate(selectedDate)

        selectedMonth = LocalDate.now().month

        selectedMonthTextView.text = MonthRename.fromMonth(selectedMonth).toString()

        lastMonthButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goBackMonth()
            }
        }

        nextMonthButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goNextMonth()
            }
        }

        lastDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goBackDays()
            }

        }

        nextDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goNextDate()
            }
        }

        totalStepCountButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                thatMonthStepCount(healthConnectClient)
            }
        }

        lastDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goBackDays()
            }
        }

        val q = LocalDateTime.now().month.value
        val month = Month.of(q)

        println("++++++++$month+++++++++")
        ////
        val monthStart =
            LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
                .withNano(0).toInstant(ZoneOffset.UTC)

        CoroutineScope(Dispatchers.Main).launch {
            checkPermissionAndRun()
            selectedDaysStepsCount(healthConnectClient, selectedDate)
//            aggregateStepsIntoMonths(
//                healthConnectClient,
//                LocalDateTime.now().withDayOfMonth(1),
//                LocalDateTime.now()
//            )
            selectedMonthStepCount(healthConnectClient, selectedMonth)
        }
    }

    // burayi incele burda istedigim verileri tam tamina alabiliyorum. for'a gerek kalmadan.
    private suspend fun aggregateStepsIntoMonths(
        healthConnectClient: HealthConnectClient,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ) {
        try {

            val startOfMonth = LocalDate.of(LocalDate.now().year, selectedMonth, 1).atStartOfDay()
            val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

            val response = healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startOfMonth, endOfMonth),
                    timeRangeSlicer = Period.ofDays(1)
                )
            )

            for (dailyResults in response) {
                // The result may be null if no data is available in the time range
                val startDays = dailyResults.startTime
                val endDays = dailyResults.endTime
                val totalSteps = dailyResults.result[StepsRecord.COUNT_TOTAL]
                println(
                    "data = $totalSteps, Times = startDate : ${formatDate(startDays.toLocalDate())} endDate : ${
                        formatDate(
                            endDays.toLocalDate()
                        )
                    }"
                )
            }

        } catch (e: Exception) {
            // Run error handling here
            e.printStackTrace()
        }

    }

    private suspend fun exampleDays(
        healthConnectClient: HealthConnectClient,
        year: Int,
        month: Int
    ) {
        try {
            // Başlangıç ve bitiş zamanlarını oluşturun
            val startOfMonth = LocalDate.of(LocalDate.now().year, selectedMonth, 1).atStartOfDay()
            val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

            // Ay boyunca her gün için adım verilerini toplayın
            var currentDate = startOfMonth.toLocalDate()

            val data: ArrayList<Long?> = arrayListOf()

            // Sağlık bağlantısından verileri toplayın
            val response = healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfMonth,
                        endOfMonth
                    ),
                    Period.ofDays(1)
                )
            )

            for (dailyData in response) {
                data.add(dailyData.result[StepsRecord.COUNT_TOTAL])
            }


            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)

            listView.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun goNextDate() {
        if (selectedDate == LocalDate.now()) {
            return
        } else {
            selectedDate = selectedDate.plusDays(1)
            updateUI()

            selectedDaysStepsCount(healthConnectClient, selectedDate)
        }
    }

    private suspend fun goBackDays() {
        selectedDate = selectedDate.minusDays(1)
        updateUI()
        selectedDaysStepsCount(healthConnectClient, selectedDate)
    }

    private suspend fun goNextMonth() {
        if (selectedMonth == LocalDate.now().month) {
            return
        } else {
            selectedMonth = selectedMonth.plus(1)
            updateUI()
            selectedMonthStepCount(healthConnectClient, selectedMonth)
        }
    }

    private suspend fun goBackMonth() {
        if (selectedMonth != Month.JANUARY) {

            selectedMonth = selectedMonth.minus(1)
            updateUI()
            selectedMonthStepCount(healthConnectClient, selectedMonth)

        }

    }

    private fun updateUI() {
        selectedDaysTextView.text = formatDate(selectedDate)
        selectedMonthTextView.text = MonthRename.fromMonth(selectedMonth).toString()
    }

    private fun formatDate(date: LocalDate): String {

        val formattedDate = DateTimeFormatter.ofPattern("d MMMM yyyy")

        return date.format(formattedDate) ?: "N/a"

    }

    private suspend fun checkPermissionAndRun() {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()

        if (granted.containsAll(dataPermission)) {
//            // permission al ready granted read or write data
//            val time = LocalDateTime.now()
//            val daysAgo = time.minusDays(10)
//            val instant = daysAgo.toInstant(ZoneOffset.UTC)
//            thatMountStepCount(
//                healthConnectClient,
//            )
//
//
//            aggregateHeart(
//                healthConnectClient,
//                startTime = instant,
//                endTime = Instant.now(),
//            )
//
//            aggregateSpeed(
//                healthConnectClient,
//                startTime = instant,
//                endTime = Instant.now(),
//            )

        } else {
            requestPermission.launch(dataPermission)
        }

    }

    private suspend fun thatMonthStepCount(
        healthConnectClient: HealthConnectClient,
    ) {

        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        val endOfDay = LocalDateTime.now()

        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startOfMonth, endOfDay)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[StepsRecord.COUNT_TOTAL]

            println("step count read value : $stepCount")
            stepCountTextView.text = (stepCount ?: "N/a").toString()
            selectedDaysTextView.text = formatDate(LocalDate.now())
        } catch (e: Exception) {
            // Run error handling here
            println("=====================  aggregateSteps error : $e ")

        }
    }

    private suspend fun selectedDaysStepsCount(
        healthConnectClient: HealthConnectClient,
        selectedDate: LocalDate
    ) {

        val startTime = selectedDate.atStartOfDay()

        val enOfDay = selectedDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)

        try {

            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime,
                        enOfDay
                    )
                )
            )

            val stepsCount = response[StepsRecord.COUNT_TOTAL]
            stepCountTextView.text = (stepsCount ?: "N/a").toString()

        } catch (e: Exception) {
            println(e.printStackTrace())
        }

    }

    private suspend fun selectedMonthStepCount(
        healthConnectClient: HealthConnectClient,
        selectedMonth: Month?
    ) {
        try {
            // Başlangıç ve bitiş zamanlarını oluşturun
            val startOfMonth = LocalDate.of(LocalDate.now().year, selectedMonth, 1).atStartOfDay()
            val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

            // Ay boyunca her gün için adım verilerini toplayın
            var currentDate = startOfMonth.toLocalDate()

            val data: ArrayList<Long?> = arrayListOf()

            // Sağlık bağlantısından verileri toplayın
            val response = healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfMonth,
                        endOfMonth
                    ),
                    Period.ofDays(1)
                )
            )

            for (dailyData in response) {
                data.add(dailyData.result[StepsRecord.COUNT_TOTAL])
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)

            listView.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}