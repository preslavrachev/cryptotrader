package com.preslavrachev.cryptotrader.trading.instrument.timeline

import java.time.LocalDateTime

data class TimelineNode<out T>(
        val time: LocalDateTime,
        val content: T
)
