package com.preslavrachev.cryptotrader.trading.strategy.impl

import com.preslavrachev.cryptotrader.calculateLocalDateTime
import com.preslavrachev.cryptotrader.config.MainAppConfig
import com.preslavrachev.cryptotrader.extension.minusSecondPeriods
import com.preslavrachev.cryptotrader.extension.toCandlestick
import com.preslavrachev.cryptotrader.extension.toPairs
import com.preslavrachev.cryptotrader.extension.toUnixTimestamp
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode
import com.preslavrachev.cryptotrader.trading.strategy.TradingStrategyDecisionEnum
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import remote.poloniex.model.ChartDataEntry
import remote.poloniex.service.PoloniexApiService
import remote.poloniex.service.PoloniexApiService.CurrencyPairEnum.*
import java.time.LocalDateTime
import javax.inject.Inject


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(MainAppConfig::class))
class CandlestickPatternTradingStrategyITest {

    data class PriceAndTimstamp(val price: Float, val timestamp: Long)

    val strategy: CandlestickPatternTradingStrategy = CandlestickPatternTradingStrategy()

    @Inject
    lateinit var poloniexApiService: PoloniexApiService

    @Before
    fun setUp() {
    }

    @Ignore("Use this test manual evaluations only")
    @Test
    fun testDecisionPotential() {
        val end = LocalDateTime.now().toUnixTimestamp()
        val start = end.minusSecondPeriods(1000, 300)
        val chartData = poloniexApiService.getChartData(currencyPair = USDT_BTC.label, start = start, end = end)

        val pairs = chartData.toPairs()
        assertTrue(pairs.all { it.second.date - it.first.date == 300L })

        val decisionsAndPrices = pairs.toDecisionsandPrices()
        print(decisionsAndPrices)

        val decisions = decisionsAndPrices.map { it.first }

        val totalDecisionCount = decisions.count().toFloat()
        val buyDecisionCount = decisions.filter { it == TradingStrategyDecisionEnum.BUY }.count().toFloat()
        val sellDecisionCount = totalDecisionCount - buyDecisionCount

        val averageBuyingPrice = decisionsAndPrices.filter { it.first == TradingStrategyDecisionEnum.BUY }.map { it.second.price }.average()
        val averageSellingPrice = decisionsAndPrices.filter { it.first == TradingStrategyDecisionEnum.SELL }.map { it.second.price }.average()

        println("Decision distribution: BUY -> ${buyDecisionCount/totalDecisionCount*100}% | SELL: ${sellDecisionCount/totalDecisionCount*100}%")
        println("Average prices: BUY: $averageBuyingPrice | SELL: $averageSellingPrice")
        println(decisionsAndPrices)

        var baseCurrencyAmount = 100f
        var quoteCurrencyAmount = 0.001f

        println("Starting balance -- total: ${baseCurrencyAmount + quoteCurrencyAmount * chartData.first().weightedAverage} " +
                " base currency: $baseCurrencyAmount | quote currency: $quoteCurrencyAmount")

        decisionsAndPrices.forEach {
            if (it.first == TradingStrategyDecisionEnum.BUY) {
                val quoteAmountToBuy = baseCurrencyAmount / it.second.price * 0.05f
                quoteCurrencyAmount += quoteAmountToBuy
                baseCurrencyAmount -= quoteAmountToBuy * it.second.price
            } else if (it.first == TradingStrategyDecisionEnum.SELL) {
                val quoteAmountToSell = quoteCurrencyAmount * 0.05f
                baseCurrencyAmount += quoteAmountToSell * it.second.price
                quoteCurrencyAmount -= quoteAmountToSell
            }
        }

        println("Final balance -- total: ${baseCurrencyAmount + quoteCurrencyAmount * chartData.last().weightedAverage} " +
                " base currency: $baseCurrencyAmount | quote currency: $quoteCurrencyAmount")
    }

    private fun List<Pair<ChartDataEntry, ChartDataEntry>>.toDecisionsandPrices(): List<Pair<TradingStrategyDecisionEnum, PriceAndTimstamp>> {
        return this.map { (prev, current) ->
            listOf(
                    TimelineNode(prev.calculateLocalDateTime(), prev.toCandlestick()),
                    TimelineNode(current.calculateLocalDateTime(), current.toCandlestick())
            )
        }
        .map {
            val decision = strategy.decide(it)
            val currentCandlestick = it.last().content
            val price = currentCandlestick.close
            Pair(decision, PriceAndTimstamp(price, it.last().time.toUnixTimestamp()))
        }
        .filter { it.first != TradingStrategyDecisionEnum.NO_DECISION }
    }

}
