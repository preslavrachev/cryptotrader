package com.preslavrachev.cryptotrader.mvc.controller
import com.preslavrachev.cryptotrader.extension.minusSecondPeriods
import com.preslavrachev.cryptotrader.extension.toUnixTimestamp
import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.session.AppSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import remote.poloniex.service.PoloniexApiService
import remote.poloniex.service.PoloniexApiService.CurrencyPairEnum.*
import java.time.LocalDateTime
import javax.inject.Inject

@Controller
@RequestMapping("/")
class DashboardController {

    @Inject
    lateinit var session:AppSession

    @Inject
    lateinit var poloniexApiService: PoloniexApiService

    @RequestMapping("hello", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun hello(): PoloniexApiService.ChartDataEntryList {
        val end = LocalDateTime.now().toUnixTimestamp()
        val start = end.minusSecondPeriods(100, 300)
        return poloniexApiService.getChartData(currencyPair = USDT_BTC.label, start = start, end = end)
    }

    @GetMapping("/balances")
    @ResponseBody
    fun returnBalances(): Any {
        return poloniexApiService.returnBalances()
    }

    @GetMapping("orders")
    @ResponseBody
    fun orders(): List<Order> {
        return session.orders
    }
}
