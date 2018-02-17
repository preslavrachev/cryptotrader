package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum.DEMO
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum.REAL
import com.preslavrachev.cryptotrader.trading.api.TradingManagement
import org.springframework.stereotype.Component

@Component
class TradingManagementFactory(val demoService: DemoTradingService) {
    fun getTradingService(orderScope: OrderScopeEnum): TradingManagement {
        return when(orderScope) {
            DEMO -> demoService
            REAL -> TODO("Not implemented yet")
        }
    }
}
