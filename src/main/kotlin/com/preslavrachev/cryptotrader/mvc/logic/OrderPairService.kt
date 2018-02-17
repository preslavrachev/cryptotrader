package com.preslavrachev.cryptotrader.mvc.logic

import com.preslavrachev.cryptotrader.mvc.controller.OrderPairWebRequest
import com.preslavrachev.cryptotrader.trading.TradingManagementFactory
import org.springframework.stereotype.Service

@Service
class OrderPairService(val tradingManagementFactory: TradingManagementFactory) {
    fun placeOrderPair(tickerPair: String, request: OrderPairWebRequest) {
        TODO("To be implemented")
    }
}
