package com.preslavrachev.cryptotrader.trading.strategy

import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode

interface TradingStrategy<in T> {
    fun decide(input: List<TimelineNode<T>>): TradingStrategyDecisionEnum
}
