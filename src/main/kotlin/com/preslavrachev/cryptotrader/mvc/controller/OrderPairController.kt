package com.preslavrachev.cryptotrader.mvc.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.preslavrachev.cryptotrader.mvc.controller.PairType.BUY_SELL
import com.preslavrachev.cryptotrader.mvc.logic.OrderPairService
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum
import com.preslavrachev.cryptotrader.mvc.model.OrderScopeEnum.DEMO
import org.springframework.web.bind.annotation.*

enum class PairType(@JsonValue val id: Long) {
    BUY_SELL(id = 1L),
    SELL_BUY(id = 2L)
    ;

    companion object {
        /**
         * JSON creator
         * NOTE: when giving the creator function parameter the same name as the underlying property we're about to
         * check, the method would not work. Changing the name (to @{link requestId} in this case) seems to have done the job
         *
         * @see <a href="https://github.com/FasterXML/jackson-module-kotlin/issues/75" />
         * @see <a href="https://github.com/FasterXML/jackson-module-kotlin/issues/78" />
         */
        @JsonCreator
        @JvmStatic
        fun fromId(requestId: Long): PairType {
            return values().first { it.id == requestId }
        }
    }
}

// @formatter:off
data class OrderPairWebRequest(
        val quoteAmount: Float,
        val type: PairType = BUY_SELL,
        val scope: OrderScopeEnum = DEMO)
// @formatter:on

@RestController
@RequestMapping("/orderPair")
class OrderPairController(val service: OrderPairService) {

    @PostMapping("/{tickerPair}")
    fun placeOrderPair(@PathVariable(name = "tickerPair") tickerPair: String, @RequestBody request: OrderPairWebRequest): Any {

        service.placeOrderPair(tickerPair, request)
        return request
    }

}
