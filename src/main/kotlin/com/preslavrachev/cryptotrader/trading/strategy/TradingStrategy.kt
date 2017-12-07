package com.preslavrachev.cryptotrader.trading.strategy

import com.preslavrachev.cryptotrader.mvc.model.CurrencyPair
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineContent
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode

data class TradingStrategyBalance(val currencyPair: CurrencyPair, val baseAmount: Float, val quoteAmount: Float)
data class TradingStrategyOutput(val decision: TradingStrategyDecisionEnum, val amount: Float? = null)

/**
 * A convenience stereotype interface for easier implementation detection and casting
 */
interface TradingStrategyParams

/**
 * PLAN: Consider, eventually making balance and params non-optional
 */
interface TradingStrategy {
    fun decide(
            timeline: List<TimelineNode<TimelineContent>>,
            balance: TradingStrategyBalance? = null,
            params: TradingStrategyParams? = null): TradingStrategyOutput
}
