package com.example.healt_connect_test_app.features.personalDatas

import MonthRename
import android.annotation.SuppressLint
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
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import com.example.healt_connect_test_app.R
import com.example.healt_connect_test_app.app_constants.DataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
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

    private lateinit var adapter: ArrayAdapter<Long?>

    private lateinit var nextWeekButton: Button

    private lateinit var lastWeekButton: Button

    private lateinit var weeklyDateAndStepsCountTextView: TextView

    private var weeklyDataControlValue = 0

    private lateinit var dailyData: DataType
    private lateinit var weeklyData: DataType
    private lateinit var monthlyData: DataType
    private lateinit var monthlyTotalSteps: DataType
    private lateinit var allStepsCount: DataType

    @SuppressLint("SetTextI18n")
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
        nextWeekButton = findViewById(R.id.nextWeek)
        lastWeekButton = findViewById(R.id.lastWeek)
        weeklyDateAndStepsCountTextView = findViewById(R.id.weeklyStepsDateAndCount)

        dailyData = DataType.DAILY

        weeklyData = DataType.WEEKLY

        monthlyData = DataType.MONTHLY

        monthlyTotalSteps = DataType.MONTHLY_TOTAL

        allStepsCount = DataType.ALL_STEPS

        totalStepCountButton = findViewById(R.id.total_count_button)

        selectedMonthTextView = findViewById(R.id.selectedMonthTextView)

        listView = findViewById(R.id.dalyData)
        selectedDate = LocalDate.now()

        selectedDaysTextView = findViewById(R.id.selectedDate)

        selectedDaysTextView.text = formatDateDailyOrMonthly(selectedDate)

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
                goNextDays()
            }
        }

        totalStepCountButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val totalStepCount = monthlyTotalSteps.selectDataType(
                    healthConnectClient,
                    selectedDate,
                    selectedMonth
                ).invoke(healthConnectClient, selectedDate, selectedMonth)

                if (totalStepCount is AggregationResult) {
                    stepCountTextView.text = totalStepCount[StepsRecord.COUNT_TOTAL].toString()
                }
            }
        }

        lastDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                goBackDays()
            }
        }

        nextWeekButton.setOnClickListener {
            println(weeklyDataControlValue)
            CoroutineScope(Dispatchers.Main).launch {
                val elements =
                    weeklyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                        .invoke(healthConnectClient, selectedDate, selectedMonth)
                if (elements is List<*> && AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                        elements.firstOrNull()?.javaClass
                    )
                ) {

                    val weeklyResult: ArrayList<AggregationResultGroupedByPeriod> = arrayListOf()

                    for (element in elements) {
                        if (element is AggregationResultGroupedByPeriod) {
//                            println(
//                                "Personal Data weekly Step count : ${element.result[StepsRecord.COUNT_TOTAL]} startDate : ${
//                                    formatDateDailyOrMonthly(
//                                        element.startTime.toLocalDate()
//                                    )
//                                } endDate : ${formatDateWeeklyDataEndDate(element.endTime.toLocalDate())}"
//                            )

                            weeklyResult.add(element)

                        }
                    }

                    if (weeklyDataControlValue < weeklyResult.size - 1) {
                        weeklyDataControlValue++

                        weeklyDateAndStepsCountTextView.text =
                            ("${weeklyResult[weeklyDataControlValue].result[StepsRecord.COUNT_TOTAL]}/${
                                formatDateDailyOrMonthly(
                                    weeklyResult[weeklyDataControlValue].startTime.toLocalDate()
                                )
                            }-${formatDateWeeklyDataEndDate(weeklyResult[weeklyDataControlValue].endTime.toLocalDate())}")
                    }
                }
            }

        }

        lastWeekButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val weeklyStepsData =
                    weeklyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                        .invoke(healthConnectClient, selectedDate, selectedMonth)
                if (weeklyStepsData is List<*> && AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                        weeklyStepsData.firstOrNull()?.javaClass
                    )
                ) {

                    val weeklyResult: ArrayList<AggregationResultGroupedByPeriod> = arrayListOf()

                    for (element in weeklyStepsData) {
                        if (element is AggregationResultGroupedByPeriod) {

                            weeklyResult.add(element)

                        }

                    }

                    if (weeklyDataControlValue > 0) {
                        weeklyDataControlValue--
                        weeklyDateAndStepsCountTextView.text =
                            ("${weeklyResult[weeklyDataControlValue].result[StepsRecord.COUNT_TOTAL]}/${
                                formatDateDailyOrMonthly(
                                    weeklyResult[weeklyDataControlValue].startTime.toLocalDate()
                                )
                            }-${formatDateWeeklyDataEndDate(weeklyResult[weeklyDataControlValue].endTime.toLocalDate())}")

                    }

                }

            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            println("On create corutine scopde running")
            checkPermissionAndRun()

            val elements =
                weeklyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)

            if (elements is List<*> && AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                    elements.firstOrNull()?.javaClass
                )
            ) {

                val weeklyResult: ArrayList<AggregationResultGroupedByPeriod> = arrayListOf()

                for (element in elements) {
                    if (element is AggregationResultGroupedByPeriod) {

                        weeklyResult.add(element)

                        weeklyDateAndStepsCountTextView.text =
                            ("${weeklyResult[weeklyDataControlValue].result[StepsRecord.COUNT_TOTAL]}/${
                                formatDateDailyOrMonthly(
                                    weeklyResult[weeklyDataControlValue].startTime.toLocalDate()
                                )
                            }-${formatDateWeeklyDataEndDate(weeklyResult[weeklyDataControlValue].endTime.toLocalDate())}")

                    }

                }

            }

            val selectedDaysStepsCount =
                dailyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)

            if (selectedDaysStepsCount is AggregationResult) {
                stepCountTextView.text =
                    selectedDaysStepsCount[StepsRecord.COUNT_TOTAL].toString()
            }


            val selectedMonthStepsCount =
                monthlyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)

            if (selectedMonthStepsCount is List<*> && AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                    selectedMonthStepsCount.firstOrNull()?.javaClass
                )
            ) {
                val stepCount: ArrayList<AggregationResultGroupedByPeriod?> = arrayListOf()

                for (dailySteps in selectedMonthStepsCount) {
                    if (dailySteps is AggregationResultGroupedByPeriod) {
                        stepCount.add(dailySteps)
                    }
                }

                adapter = ArrayAdapter<Long?>(
                    this@PersonalDatas,
                    android.R.layout.simple_list_item_1,
                    stepCount.map { it?.result?.get(StepsRecord.COUNT_TOTAL) }
                )

                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(
                        this@PersonalDatas,
                        "Tiklanan elemanin tarihi : ${
                            stepCount[position]?.startTime?.toLocalDate()
                                ?.let { formatDateDailyOrMonthly(it) }
                        }",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

    }

    private suspend fun goNextDays() {
        if (selectedDate == LocalDate.now()) {
            return
        } else {
            selectedDate = selectedDate.plusDays(1)
            val stepCount =
                dailyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)

            if (stepCount is AggregationResult) {
                stepCountTextView.text = stepCount[StepsRecord.COUNT_TOTAL].toString()
            }

            updateUI()
        }

    }

    private suspend fun goBackDays() {
        selectedDate = selectedDate.minusDays(1)
        val stepCount = dailyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
            .invoke(healthConnectClient, selectedDate, selectedMonth)

        if (stepCount is AggregationResult) {
            stepCountTextView.text = stepCount[StepsRecord.COUNT_TOTAL].toString()
        }

        updateUI()

    }

    private suspend fun goNextMonth() {
        if (selectedMonth == LocalDate.now().month) {
            return
        } else {
            selectedMonth = selectedMonth.plus(1)
            updateUI()

            val selectedMonthDailyCount =
                monthlyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)
            if (selectedMonthDailyCount != null) {
                if (selectedMonthDailyCount is List<*>) {
                    if (AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                            selectedMonthDailyCount.firstOrNull()?.javaClass
                        )
                    ) {
                        val responseList: ArrayList<AggregationResultGroupedByPeriod> =
                            arrayListOf()
                        for (dailyStep in selectedMonthDailyCount) {
                            if (dailyStep is AggregationResultGroupedByPeriod) {
                                responseList.add(dailyStep)
                            }
                        }
                        if (responseList.isNotEmpty()) {
                            adapter.clear()
                            adapter.addAll(responseList.map { it.result[StepsRecord.COUNT_TOTAL] })
                        } else {
                            adapter.clear()
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            } else {
                // Veri gelmediği durumda adapter'ı temizleyebilirsiniz
                adapter.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private suspend fun goBackMonth() {
        if (selectedMonth != Month.JANUARY) {

            selectedMonth = selectedMonth.minus(1)
            val selectedMonthDailyCount =
                monthlyData.selectDataType(healthConnectClient, selectedDate, selectedMonth)
                    .invoke(healthConnectClient, selectedDate, selectedMonth)
            updateUI()

            if (selectedMonthDailyCount != null) {
                if (selectedMonthDailyCount is List<*>) {
                    if (AggregationResultGroupedByPeriod::class.java.isAssignableFrom(
                            selectedMonthDailyCount.firstOrNull()?.javaClass
                        )
                    ) {
                        val responseList: ArrayList<AggregationResultGroupedByPeriod> =
                            arrayListOf()
                        for (dailyStep in selectedMonthDailyCount) {
                            if (dailyStep is AggregationResultGroupedByPeriod) {
                                responseList.add(dailyStep)
                            }
                        }
                        if (responseList.isNotEmpty()) {
                            adapter.clear()
                            adapter.addAll(responseList.map { it.result[StepsRecord.COUNT_TOTAL] })
                        } else {
                            adapter.clear()
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            } else {
                // Veri gelmediği durumda adapter'ı temizleyebilirsiniz
                adapter.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateUI() {
        selectedDaysTextView.text = formatDateDailyOrMonthly(selectedDate)
        selectedMonthTextView.text = MonthRename.fromMonth(selectedMonth).toString()
    }

    private fun formatDateDailyOrMonthly(date: LocalDate): String {

        val formattedDate = DateTimeFormatter.ofPattern("d MMMM yyyy")

        return date.format(formattedDate) ?: "N/a"

    }

    private fun formatDateWeeklyDataEndDate(endDate: LocalDate): String {
        val formatUsingDate = endDate.minusDays(1)
        val formattedDate = DateTimeFormatter.ofPattern("d MMMM yyyy")

        return formatUsingDate.format(formattedDate) ?: "'N/a"

    }

    private suspend fun checkPermissionAndRun() {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()

        if (!granted.containsAll(dataPermission)) {
            requestPermission.launch(dataPermission)
        }

    }


}
