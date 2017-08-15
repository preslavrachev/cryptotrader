package com.preslavrachev.cryptotrader.trading.api

import com.preslavrachev.cryptotrader.mvc.model.Order

interface TradingApi {
    fun placeOrder(order: Order): Boolean
}
