package com.preslavrachev.cryptotrader.trading.instrument.timeline

import java.time.LocalDateTime

/**
 * A convenience stereotype interface for easier implementation detection and casting
 */
interface TimelineContent

data class TimelineNode<out TimelineContent>(
        val time: LocalDateTime,
        val content: TimelineContent
)
