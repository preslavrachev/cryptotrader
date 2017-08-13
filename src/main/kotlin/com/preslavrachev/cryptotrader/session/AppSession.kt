package com.preslavrachev.cryptotrader.session

import com.preslavrachev.cryptotrader.mvc.model.Order
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("singleton")
class AppSession {
    val orders = ArrayList<Order>()
}
