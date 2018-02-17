package com.preslavrachev.cryptotrader.trading.api

import com.preslavrachev.cryptotrader.mvc.model.Order

interface TradingManagement {
    fun placeOrder(order: Order): String?
}
