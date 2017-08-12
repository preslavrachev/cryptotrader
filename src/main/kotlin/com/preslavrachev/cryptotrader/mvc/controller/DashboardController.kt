package com.preslavrachev.cryptotrader.mvc.controller
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import remote.poloniex.service.PoloniexApiService
import javax.inject.Inject

@Controller
@RequestMapping("/")
class DashboardController {

    @Inject
    lateinit var poloniexApiService: PoloniexApiService

    @RequestMapping("hello")
    @ResponseBody
    fun hello(): PoloniexApiService.ChartDataEntryList {
        return poloniexApiService.getChartData()
    }

    @RequestMapping("balances", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun returnBalances(): Any {
        return poloniexApiService.returnBalances()
    }
}
