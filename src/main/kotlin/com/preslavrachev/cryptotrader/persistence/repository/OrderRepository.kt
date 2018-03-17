package com.preslavrachev.cryptotrader.persistence.repository

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderStateEnum
import org.springframework.data.mongodb.repository.MongoRepository

interface OrderRepository: MongoRepository<Order, String> {
    fun findAllByState(state: OrderStateEnum): List<Order>
}
