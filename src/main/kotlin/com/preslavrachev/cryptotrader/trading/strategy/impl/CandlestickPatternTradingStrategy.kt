package com.preslavrachev.cryptotrader.trading.strategy.impl

import com.preslavrachev.cryptotrader.trading.instrument.candlestick.Candlestick
import com.preslavrachev.cryptotrader.trading.instrument.candlestick.CandlestickPatternEnum
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode
import com.preslavrachev.cryptotrader.trading.strategy.TradingStrategy
import com.preslavrachev.cryptotrader.trading.strategy.TradingStrategyDecisionEnum
import java.util.*
import java.util.logging.Logger

class CandlestickPatternTradingStrategy : TradingStrategy<Candlestick> {

    companion object {
        val LOGGER = Logger.getLogger(CandlestickPatternTradingStrategy::class.simpleName)

        // TODO: move this knowledge over to the candlestick patterns enum
        val BOOLISH_PATTERNS = EnumSet.of(CandlestickPatternEnum.HAMMER, CandlestickPatternEnum.INVERTED_HAMMER)
        val BEARISH_PATTERNS = EnumSet.of(CandlestickPatternEnum.SHOOTING_STAR, CandlestickPatternEnum.HANGING_MAN)
    }

    override fun decide(input: List<TimelineNode<Candlestick>>): TradingStrategyDecisionEnum {
        assert(input.size >= 2) { LOGGER.warning("The input must have at least two data points!") }

        val prev = input[input.lastIndex - 1]
        val current = input[input.lastIndex]

        val pattern = CandlestickPatternEnum.evaluate(prev.content, current.content)

        if (pattern.any { BOOLISH_PATTERNS.contains(it) }) {
            return TradingStrategyDecisionEnum.BUY
        } else if (pattern.any { BEARISH_PATTERNS.contains(it) }) {
            return TradingStrategyDecisionEnum.SELL
        } else {
            return TradingStrategyDecisionEnum.NO_DECISION
        }
    }
}
