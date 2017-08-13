package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import remote.poloniex.service.PoloniexApiService
import java.util.concurrent.Callable

class OrderProcessingException(val order: Order): RuntimeException()

class OrderProcessorTask(val order: Order, val apiService: PoloniexApiService): Callable<Order> {
    override fun call(): Order {
        TODO("To be implemented")
        return order
    }
}
