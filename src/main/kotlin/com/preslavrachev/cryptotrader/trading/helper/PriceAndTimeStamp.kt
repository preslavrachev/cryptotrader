package com.preslavrachev.cryptotrader.trading.helper

import remote.poloniex.model.ChartDataEntry

data class PriceAndTimestamp(val price: Float, val timestamp: Long)

/**
 * Default implementation that uses the weighted average price
 */
fun List<ChartDataEntry>.toPricesAndTimestamps(): List<PriceAndTimestamp> {
    return toPricesAndTimestamps(ChartDataEntry::weightedAverage)
}

fun List<ChartDataEntry>.toPricesAndTimestamps(predicate: (ChartDataEntry) -> Float): List<PriceAndTimestamp> {
    return this.map { it -> PriceAndTimestamp(predicate.invoke(it), it.date) }
}
