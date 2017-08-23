package com.preslavrachev.cryptotrader.trading.instrument.candlestick

data class Candlestick(
        val open: Float,
        val close: Float,
        val high: Float,
        val low: Float
) {
    companion object {
        val MIN_HEIGHT_RATIO = 0.3
        val MAX_HEIGHT_RATIO = 0.7
        val ONE_HALF = 0.5
    }

    fun bodyHeight(): Float {
        return Math.abs(open - close)
    }

    fun shadowHeight(): Float {
        return Math.abs(high - low)
    }

    private fun bodyMidpoint(): Float {
        return ((ONE_HALF *  (open + close)) - low).toFloat()
    }

    fun isRed(): Boolean {
        return open > close
    }

    fun isGreen(): Boolean {
        return close > open
    }

    fun isDoji(): Boolean {
        return bodyHeight() <= MIN_HEIGHT_RATIO * shadowHeight()
    }

    fun isBodyBottom(): Boolean {
        return bodyMidpoint() <= (MIN_HEIGHT_RATIO * shadowHeight())
    }

    fun isBodyTop(): Boolean {
        return bodyMidpoint() >= (MAX_HEIGHT_RATIO * shadowHeight())
    }
}
