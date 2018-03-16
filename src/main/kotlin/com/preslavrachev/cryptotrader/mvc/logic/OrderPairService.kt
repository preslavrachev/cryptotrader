package com.preslavrachev.cryptotrader.mvc.logic

import com.preslavrachev.cryptotrader.mvc.controller.OrderPairWebRequest
import com.preslavrachev.cryptotrader.mvc.controller.PairType
import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum.BUY
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum.SELL
import org.springframework.stereotype.Service

@Service
class OrderPairService(val orderService: MiddlewareOrderService) {
    fun placeOrderPair(tickerPair: String, request: OrderPairWebRequest) {
        val (baseCurrency, quoteCurrency) = tickerPair.split("_")

        //  curries the boilerplate
        val orderCreator: (OrderTypeEnum) -> Order = { orderType -> Order(orderType, 10f, quoteCurrency, baseCurrency) }

        val (firstOrder, secondOrder) = when (request.type) {
            PairType.BUY_SELL -> Pair(orderCreator(BUY), orderCreator(SELL))
            PairType.SELL_BUY -> Pair(orderCreator(SELL), orderCreator(BUY))
        }

        val persistedFirstOrder = orderService.persist(firstOrder)
        orderService.persist(secondOrder.apply { predecessor = persistedFirstOrder.id })
    }
}
