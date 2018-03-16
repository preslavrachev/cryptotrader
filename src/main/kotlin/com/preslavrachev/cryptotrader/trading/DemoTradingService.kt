package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum
import com.preslavrachev.cryptotrader.trading.api.TradingManagement
import org.springframework.stereotype.Service

@Service
class DemoTradingService : TradingManagement {
    override fun placeOrder(order: Order): String? {
        val scopedOrder = order.copy(scope=OrderScopeEnum.DEMO)
        TODO("Implement the rest of the functionality")
    }
}
