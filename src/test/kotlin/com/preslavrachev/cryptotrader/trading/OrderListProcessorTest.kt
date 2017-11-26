package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderStateEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ForkJoinPool
import java.util.logging.Logger

class OrderListProcessorTest {

    companion object {
        val LOGGER = Logger.getLogger("OrderListProcessorTest")
        val ORDER_AMOUNT = 10.0f
        val QUOTE_CURRENCY = "BTC"
        val BASE_CURRENCY = "USDT"
    }

    lateinit var orderListProcessor: OrderListProcessor

    @Before
    fun setUp() {
        orderListProcessor = OrderListProcessor()
        orderListProcessor.tradingApi = DemoTradingService()
        orderListProcessor.postConstruct()
    }

    @Test
    fun theOrderProcessing() {
        orderListProcessor.orderProcessingPool = ForkJoinPool(5)
        val orders = listOf(
                Order(type = OrderTypeEnum.BUY, amount = ORDER_AMOUNT, quoteCurrency = QUOTE_CURRENCY, baseCurrency = BASE_CURRENCY),
                Order(type = OrderTypeEnum.SELL, amount = ORDER_AMOUNT, quoteCurrency = QUOTE_CURRENCY, baseCurrency = BASE_CURRENCY)
        )

        val processedOrders = orderListProcessor.processOrders(orders)
                .collectList()
                .block()!!

        Assert.assertTrue(processedOrders.all { it.state == OrderStateEnum.DONE })
    }
}

