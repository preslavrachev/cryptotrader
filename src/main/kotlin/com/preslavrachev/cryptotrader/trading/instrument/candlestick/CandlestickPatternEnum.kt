package com.preslavrachev.cryptotrader.trading.instrument.candlestick

enum class CandlestickPatternEnum(
        val evaluator: (prev: Candlestick, current:Candlestick) -> Boolean
) {
    // @formatter:off
    HAMMER          ({prev, current -> isHammerShape(current) && current.isBodyTop() && prev.isRed() && !isHammerShape(prev) }),
    INVERTED_HAMMER ({prev, current -> isHammerShape(current) && current.isBodyBottom() && prev.isRed() && !isHammerShape(prev)}),
    SHOOTING_STAR   ({prev, current -> isHammerShape(current) && current.isBodyBottom() && prev.isGreen() && !isHammerShape(prev)}),
    HANGING_MAN     ({prev, current -> isHammerShape(current) && current.isBodyTop() && prev.isGreen() && !isHammerShape(prev)}),
    ;
    // @formatter:on

    companion object {
        private val BODY_SHADOW_MAX_HAMMER_RATIO = 0.6

        fun evaluate(prev: Candlestick, current: Candlestick): List<CandlestickPatternEnum> {
            return CandlestickPatternEnum.values()
                    .filter { it.evaluator(prev, current) }
        }

        fun isHammerShape(candlestick: Candlestick): Boolean {
            return candlestick.bodyHeight() <= BODY_SHADOW_MAX_HAMMER_RATIO * candlestick.shadowHeight()
        }
    }
}
