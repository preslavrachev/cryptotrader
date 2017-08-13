package com.preslavrachev.cryptotrader.mvc.model

import java.time.LocalDateTime

data class Order(
        val type: OrderTypeEnum,
        val amount: Float, /* always in relation to the quote currency */
        val quoteCurrency: String, /* e.g. BTC in BTC/USDT */
        val baseCurrency: String, /* e.g. USDT in BTC/USDT */
        val executionDateTime: LocalDateTime = LocalDateTime.now(),
        var state: OrderStateEnum = OrderStateEnum.NEW,
        val scope: OrderScopeEnum = OrderScopeEnum.DEMO
)