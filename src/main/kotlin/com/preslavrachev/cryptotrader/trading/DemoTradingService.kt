package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.trading.api.TradingApi
import org.springframework.stereotype.Service

@Service
class DemoTradingService: TradingApi {
    override fun placeOrder(order: Order): Boolean {
        TODO("not implemented")
    }
}