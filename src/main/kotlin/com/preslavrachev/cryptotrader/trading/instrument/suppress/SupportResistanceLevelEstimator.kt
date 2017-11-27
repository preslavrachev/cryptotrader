package com.preslavrachev.cryptotrader.trading.instrument.suppress

import com.preslavrachev.cryptotrader.trading.helper.PriceAndTimestamp

/**
 * A helper data tuple class
 */
private data class PriceSliceAndTimeScore(val price: Float, val slice: Int, val timeScore: Long)

/**
 * Estimates support/resistance levels (https://en.wikipedia.org/wiki/Support_and_resistance) by:
 * 1. Vertically splitting the price range into a number (default is 100) of equally thick slices
 * 2. The data points in each of the slices get the square distance of their times used as a measure of the "horizontal
 * spreadiness" of the points within each slice. The "spreadiness" indicates that a certain prices has been reached
 * before (regardless of the direction of the price trend), passed, and a price reversal has followed. Such levels are
 * ideal candidates for support/resistance lines
 * 3. The slice with the greatest level of "spreadiness" (the sum of all squared times) gets selected, and the average
 * price of all data points lying within it, gets returned.
 */
class SupportResistanceLevelEstimator {

    companion object {
        val NUMBER_OF_SLICES = 100
    }

    fun estimateSupportResistanceLevel(input: List<PriceAndTimestamp>): Float {

        val priceSelector: (PriceAndTimestamp) -> Float = { (price) -> price }
        val minPrice = input.minBy(priceSelector)!!.price
        val maxPrice = input.maxBy(priceSelector)!!.price

        val output = input.map { it ->
            val slice: Int = ((it.price - minPrice) / (maxPrice - minPrice) * 100).toInt()
            val timeScore: Long = Math.pow((it.timestamp / NUMBER_OF_SLICES).toDouble(), 2.0).toLong()
            PriceSliceAndTimeScore(it.price, slice, timeScore)
        }.groupBy(PriceSliceAndTimeScore::slice).map { it ->
            Pair(it.value.map(PriceSliceAndTimeScore::price).average(), it.value.sumByDouble { (timeScore) -> timeScore.toDouble() })
        }.maxBy { it -> it.second }

        return output!!.first.toFloat()
    }
}
