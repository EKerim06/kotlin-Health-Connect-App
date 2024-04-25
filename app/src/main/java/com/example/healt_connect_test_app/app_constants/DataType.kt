package com.example.healt_connect_test_app.app_constants

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.LocalDate
import java.time.Period

enum class DataType {
    DAILY,
    WEEKLY,
    MONTHLY;

    suspend fun selectDataType(
        healthConnectClient: HealthConnectClient,
        selectedDate: LocalDate,
    ): suspend (HealthConnectClient, LocalDate) -> Any? {
        return when (this) {
            DAILY -> ::selectedDaysStepsCount
            WEEKLY -> ::weeklyStepData
            MONTHLY -> ::selectedMonthStepCount
        }
    }


    private suspend fun selectedDaysStepsCount(
        healthConnectClient: HealthConnectClient,
        selectedDate: LocalDate
    ): String {
        val startTime = selectedDate.atStartOfDay()
        val enOfDay = selectedDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)

        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime,
                        enOfDay
                    )
                )
            )
            response[StepsRecord.COUNT_TOTAL]?.toString() ?: "Veri girisi yok"
        } catch (e: Exception) {
            e.printStackTrace()
            e.message ?: "Veri okuma hatasi"
        }
    }

    private suspend fun selectedMonthStepCount(
        healthConnectClient: HealthConnectClient,
        thisSelectedMonth: LocalDate
    ): ArrayList<Long?>? {
        return try {
            // Başlangıç ve bitiş zamanlarını oluşturun
            val startOfMonth =
                thisSelectedMonth.withDayOfMonth(1).atStartOfDay()
            val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

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

            data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun weeklyStepData(
        healthConnectClient: HealthConnectClient,
        startTime: LocalDate
    ): List<AggregationResultGroupedByPeriod>? {
        val startOfMonth = LocalDate.of(LocalDate.now().year, startTime.month, 1).atStartOfDay()
        val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

        return try {
            val response = healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startOfMonth, endOfMonth),
                    timeRangeSlicer = Period.ofWeeks(1)
                )
            )

            for (weeklyData in response) {
                println(
                    "Weekly step count : ${weeklyData.result[StepsRecord.COUNT_TOTAL]} || startDate : ${weeklyData.startTime.toLocalDate()}; endDate : ${weeklyData.endTime.toLocalDate()}"
                )
            }
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}