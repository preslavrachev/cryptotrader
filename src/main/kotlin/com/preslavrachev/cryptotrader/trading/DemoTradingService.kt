package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderStateEnum
import com.preslavrachev.cryptotrader.trading.api.TradingManagement
import org.springframework.stereotype.Service

@Service
class DemoTradingService : TradingManagement {

    companion object {
        const val SUCCESS_THRESHOLD = 0.1f
    }

    override fun placeOrder(order: Order): Order {
        val scopedOrder = order.copy(scope=OrderScopeEnum.DEMO)
        scopedOrder.state = OrderStateEnum.PROCESSING

        if (Math.random() >= SUCCESS_THRESHOLD) {
            scopedOrder.state = OrderStateEnum.DONE
        } else {
            scopedOrder.state = OrderStateEnum.FAILED
        }

        return scopedOrder
    }
}
