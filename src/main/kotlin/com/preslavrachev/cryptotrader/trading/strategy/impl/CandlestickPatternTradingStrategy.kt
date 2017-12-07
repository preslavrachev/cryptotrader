package com.preslavrachev.cryptotrader.trading.strategy.impl

import com.preslavrachev.cryptotrader.trading.instrument.candlestick.Candlestick
import com.preslavrachev.cryptotrader.trading.instrument.candlestick.CandlestickPatternEnum
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineContent
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode
import com.preslavrachev.cryptotrader.trading.strategy.*
import java.util.*
import java.util.logging.Logger

private typealias output = TradingStrategyOutput

class CandlestickPatternTradingStrategy : TradingStrategy {

    companion object {
        val LOGGER = Logger.getLogger(CandlestickPatternTradingStrategy::class.simpleName)!!

        // TODO: move this knowledge over to the candlestick patterns enum
        val BOOLISH_PATTERNS = EnumSet.of(CandlestickPatternEnum.HAMMER, CandlestickPatternEnum.INVERTED_HAMMER)!!
        val BEARISH_PATTERNS = EnumSet.of(CandlestickPatternEnum.SHOOTING_STAR, CandlestickPatternEnum.HANGING_MAN)!!
    }

    override fun decide(timeline: List<TimelineNode<TimelineContent>>, balance: TradingStrategyBalance?, params: TradingStrategyParams?): output {
        assert(timeline.size >= 2) { LOGGER.warning("The input must have at least two data points!") }

        @Suppress("UNCHECKED_CAST")
        val candleSticks = timeline.filter { it.content is Candlestick } as List<TimelineNode<Candlestick>>

        val prev = candleSticks[candleSticks.lastIndex - 1]
        val current = candleSticks[candleSticks.lastIndex]

        val pattern = CandlestickPatternEnum.evaluate(prev.content, current.content)

        return when {
            pattern.any { BOOLISH_PATTERNS.contains(it) } -> output(TradingStrategyDecisionEnum.BUY)
            pattern.any { BEARISH_PATTERNS.contains(it) } -> output(TradingStrategyDecisionEnum.SELL)
            else -> output(TradingStrategyDecisionEnum.NO_DECISION)
        }
    }
}
