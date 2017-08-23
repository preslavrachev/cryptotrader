package com.preslavrachev.cryptotrader.trading.instrument.candlestick

enum class CandlestickPatternEnum(
        val evaluator: (prev: Candlestick, current:Candlestick) -> Boolean
) {
    HAMMER          ({prev, current -> isHammerShape(prev) && prev.isBodyTop() && current.isGreen() && !current.isDoji()}),
    INVERTED_HAMMER ({prev, current -> isHammerShape(prev) && prev.isBodyBottom() && current.isGreen() && !current.isDoji()}),
    SHOOTING_STAR   ({prev, current -> isHammerShape(prev) && prev.isBodyBottom() && current.isRed() && !current.isDoji()}),
    HANGING_MAN     ({prev, current -> isHammerShape(prev) && prev.isBodyTop() && current.isRed() && !current.isDoji()}),
    ;

    companion object {
        fun evaluate(prev: Candlestick, current: Candlestick): List<CandlestickPatternEnum> {
            return CandlestickPatternEnum.values()
                    .filter { it.evaluator(prev, current) }
        }

        fun isHammerShape(candlestick: Candlestick): Boolean {
            return candlestick.bodyHeight() <= Candlestick.ONE_HALF * candlestick.shadowHeight()
        }
    }
}
