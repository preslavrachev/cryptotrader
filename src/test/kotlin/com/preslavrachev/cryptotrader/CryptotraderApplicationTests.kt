package com.preslavrachev.cryptotrader

import com.preslavrachev.cryptotrader.mvc.logic.MiddlewareOrderService
import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderStateEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum
import com.preslavrachev.cryptotrader.persistence.repository.OrderRepository
import com.preslavrachev.cryptotrader.trading.OrderListProcessorTest
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@EnableScheduling
class CryptotradesrApplicationTests {

    @Autowired
    lateinit var middlewareOrderService: MiddlewareOrderService

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
	fun contextLoads() {
	}

    @Test
    fun testOrderProcessing() {
        val originalOrders = generateOrders()
        originalOrders.forEach { order -> middlewareOrderService.persist(order) }

        Thread.sleep(1000)
        val storedOrders = orderRepository.findAll()
        println(storedOrders.map(Order::state))
        Assertions.assertThat(storedOrders.size).isEqualTo(originalOrders.size)
    }

    private fun generateOrders(): List<Order> {
        // @formatter:off
        return (1..10).map { Order(
                state = OrderStateEnum.NEW,
                type = OrderTypeEnum.BUY,
                amount = OrderListProcessorTest.ORDER_AMOUNT,
                quoteCurrency = OrderListProcessorTest.QUOTE_CURRENCY,
                baseCurrency = OrderListProcessorTest.BASE_CURRENCY
        ) }
        // @formatter:on
    }
}
