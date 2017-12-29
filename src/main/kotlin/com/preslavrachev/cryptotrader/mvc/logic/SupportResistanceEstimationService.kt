package com.preslavrachev.cryptotrader.mvc.logic

import com.preslavrachev.cryptotrader.extension.minusSecondPeriods
import com.preslavrachev.cryptotrader.extension.toCandlestick
import com.preslavrachev.cryptotrader.extension.toUnixTimestamp
import com.preslavrachev.cryptotrader.trading.helper.PriceAndTimestamp
import com.preslavrachev.cryptotrader.trading.helper.toPricesAndTimestamps
import com.preslavrachev.cryptotrader.trading.instrument.suppress.SupportResistanceLevelEstimator
import org.springframework.stereotype.Service
import remote.poloniex.model.ChartDataEntry
import remote.poloniex.service.PoloniexApiService
import java.time.LocalDateTime
import javax.inject.Inject

data class SupportResistanceResponse(val support: Float, val resistance: Float, val weightedAverage: Float)

@Service
class SupportResistanceEstimationService {

    companion object {
        val FILTER_FACTOR = 2
    }

    @Inject
    private lateinit var dataProvider: PoloniexApiService

    @Inject
    private lateinit var estimator: SupportResistanceLevelEstimator

    fun estimateSupportResistanceForTradingPair(tradingPair: String, numberOfDataPoints: Long): SupportResistanceResponse {
        val end = LocalDateTime.now().toUnixTimestamp()
        val start = end.minusSecondPeriods(numberOfDataPoints, 300)

        val dataPoints = dataProvider.getChartData(tradingPair, start, end)

        /* In order to achieve a good spread between the support and resistance, we sort them and take the lower
         and upper halves respectively */
        val numberOfSamplePointsToTake: Int = (numberOfDataPoints / FILTER_FACTOR).toInt()
        val lows = dataPoints.toPricesAndTimestamps(ChartDataEntry::low)
                .sortedBy(PriceAndTimestamp::price).take(numberOfSamplePointsToTake)
        val highs = dataPoints.toPricesAndTimestamps(ChartDataEntry::high)
                .sortedByDescending(PriceAndTimestamp::price).take(numberOfSamplePointsToTake)
        val avg = dataPoints.toPricesAndTimestamps(ChartDataEntry::weightedAverage)

        val support = estimator.estimateSupportResistanceLevel(lows)
        val resistance = estimator.estimateSupportResistanceLevel(highs)
        val weightedAverage = estimator.estimateSupportResistanceLevel(avg)

        return SupportResistanceResponse(support, resistance, weightedAverage)
    }

}
