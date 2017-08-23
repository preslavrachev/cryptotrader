package com.preslavrachev.cryptotrader.extension

import com.preslavrachev.cryptotrader.trading.instrument.candlestick.Candlestick
import remote.poloniex.model.ChartDataEntry

fun ChartDataEntry.toCandlestick(): Candlestick {
    return Candlestick(
            open = this.open,
            close = this.close,
            high = this.high,
            low = this.low
    )
}