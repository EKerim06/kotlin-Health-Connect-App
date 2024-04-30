package com.example.healt_connect_test_app.app_constants

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Period

enum class DataType() {
    DAILY,
    WEEKLY,
    MONTHLY_TOTAL,
    ALL_STEPS,
    MONTHLY;

    suspend fun selectDataType(
        healthConnectClient: HealthConnectClient,
        selectedDate: LocalDate,
        selectMonth: Month
    ): suspend (healthConnectClient: HealthConnectClient, selectedDate: LocalDate, selectMonth: Month) -> Any? {
        return when (this) {
            DAILY -> ::selectedDaysStepsCount
            WEEKLY -> ::weeklyStepData
            MONTHLY -> ::selectedMonthDailyStepData
            MONTHLY_TOTAL -> ::selectedMonthTotalCount
            ALL_STEPS -> ::allStepsCount
        }
    }

    private suspend fun selectedDaysStepsCount(
        healthConnectClient: HealthConnectClient,
        selectedDate: LocalDate,
        notUse: Month = Month.JANUARY
    ): AggregationResult? {

        val startTime = selectedDate.atStartOfDay()
        val endOfDay = selectedDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)

        return try {
            healthConnectClient.aggregate(
                AggregateRequest(
                    setOf(StepsRecord.COUNT_TOTAL),
                    TimeRangeFilter.between(
                        startTime,
                        endOfDay
                    )
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    private suspend fun selectedMonthDailyStepData(
        healthConnectClient: HealthConnectClient,
        notUse: LocalDate = LocalDate.now(),
        selectMonth: Month
    ): ArrayList<AggregationResultGroupedByPeriod>? {
        return try {
            // Başlangıç ve bitiş zamanlarını oluşturun
            val startOfMonth =
                LocalDate.of(LocalDate.now().year, selectMonth, 1).atStartOfDay()
            val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

            val data: ArrayList<AggregationResultGroupedByPeriod> = arrayListOf()

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
                data.add(dailyData)
            }

            if (data.isNotEmpty()) {
                data
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private suspend fun weeklyStepData(
        healthConnectClient: HealthConnectClient,
        startTime: LocalDate,
        notUse: Month = Month.JANUARY
    ): List<AggregationResultGroupedByPeriod>? {

        val startOfMonth = LocalDate.of(LocalDate.now().year, startTime.month, 1).atStartOfDay()
        val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

        val endPlusMonth = LocalDate.now().plusMonths(1).atStartOfDay()

        return try {
            val response = healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startOfMonth, endPlusMonth),
                    timeRangeSlicer = Period.ofWeeks(1)
                )
            )

//            for (weeklyData in response) {
//                println(
//                    "enum Weekly step count : ${weeklyData.result[StepsRecord.COUNT_TOTAL]} || startDate : ${weeklyData.startTime.toLocalDate()}; endDate : ${weeklyData.endTime.toLocalDate()}"
//                )
//            }

            response

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun selectedMonthTotalCount(
        healthConnectClient: HealthConnectClient,
        notUse: LocalDate,
        selectMonth: Month
    ): AggregationResult? {

        val startOfMonth =
            LocalDate.of(LocalDate.now().year, selectMonth, 1).atStartOfDay()
        val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

        return try {

            healthConnectClient.aggregate(
                AggregateRequest(
                    setOf(StepsRecord.COUNT_TOTAL),
                    TimeRangeFilter.Companion.between(startOfMonth, endOfMonth)
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private suspend fun allStepsCount(
        healthConnectClient: HealthConnectClient,
        localDate: LocalDate,
        selectMonth: Month
    ): AggregationResult? {

        return try {


            val endOfTime = Instant.now()

            healthConnectClient.aggregate(
                AggregateRequest(
                    setOf(StepsRecord.COUNT_TOTAL),
                    TimeRangeFilter.before(endOfTime)
                )
            )


        } catch (e: Exception) {

            e.printStackTrace()
            return null
        }

    }

}