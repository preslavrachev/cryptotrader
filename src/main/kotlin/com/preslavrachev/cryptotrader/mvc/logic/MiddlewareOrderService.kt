package com.preslavrachev.cryptotrader.mvc.logic

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.persistence.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class MiddlewareOrderService(val orderRepository: OrderRepository) {

    fun persist(order: Order): Order {
        return orderRepository.save(order)
    }
}
