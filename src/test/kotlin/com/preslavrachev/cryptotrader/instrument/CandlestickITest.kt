package com.preslavrachev.cryptotrader.instrument

import com.preslavrachev.cryptotrader.config.MainAppConfig
import com.preslavrachev.cryptotrader.extension.toCandlestick
import com.preslavrachev.cryptotrader.trading.instrument.candlestick.CandlestickPatternEnum
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import remote.poloniex.service.PoloniexApiService
import javax.inject.Inject


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(MainAppConfig::class))
class CandlestickITest {

    @Inject
    lateinit var poloniexApiService: PoloniexApiService

    @Ignore("Use only manually for now")
    @Test()
    fun testCandlestickPatterns(): Unit {

        val leftSide = poloniexApiService.getChartData().dropLast(1)
        val rightSide = poloniexApiService.getChartData().drop(1)
        val data = leftSide.zip(rightSide)
                .map { (prev, current) -> Pair(prev.toCandlestick(), current.toCandlestick()) }
                .map { (prev, current) -> CandlestickPatternEnum.evaluate(prev, current) }
        println("DATA: $data")
    }

}
