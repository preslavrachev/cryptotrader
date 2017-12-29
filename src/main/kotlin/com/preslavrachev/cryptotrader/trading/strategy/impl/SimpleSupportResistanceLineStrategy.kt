package com.preslavrachev.cryptotrader.trading.strategy.impl

import com.preslavrachev.cryptotrader.extension.toUnixTimestamp
import com.preslavrachev.cryptotrader.trading.helper.PriceAndTimestamp
import com.preslavrachev.cryptotrader.trading.instrument.suppress.SupportResistanceLevelEstimator
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineContent
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode
import com.preslavrachev.cryptotrader.trading.strategy.*
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class SimpleSupportResistanceLineStrategy : TradingStrategy {

    @Inject
    private lateinit var estimator: SupportResistanceLevelEstimator

    override fun decide(
            timeline: List<TimelineNode<TimelineContent>>,
            balance: TradingStrategyBalance?,
            params: TradingStrategyParams?): TradingStrategyOutput {
        val candleStickNodes = timeline.determineCandleStickNodes()!!

        estimator.estimateSupportResistanceLevel(candleStickNodes.map { (time, content) -> PriceAndTimestamp(content.close, time.toUnixTimestamp()) })

        TODO("Continue the implementation. Consider making candlestick extensible")
    }
}
