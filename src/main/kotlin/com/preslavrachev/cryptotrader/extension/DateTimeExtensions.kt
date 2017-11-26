package com.preslavrachev.cryptotrader.extension

import java.time.LocalDateTime

fun LocalDateTime.toUnixTimestamp(): Long {
    return this.toEpochSecond(java.time.ZoneOffset.ofHours(2))
}

fun Long.minusSecondPeriods(periods: Long, secondsInPeriod: Long): Long {
    val timePeriod = periods * secondsInPeriod
    assert(timePeriod <= this) { "The time period cannot exceed the current time!" }

    return this - timePeriod
}
