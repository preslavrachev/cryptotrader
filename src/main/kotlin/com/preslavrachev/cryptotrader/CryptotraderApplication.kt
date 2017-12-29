package com.preslavrachev.cryptotrader

import com.preslavrachev.cryptotrader.extension.minusSecondPeriods
import com.preslavrachev.cryptotrader.extension.toCandlestick
import com.preslavrachev.cryptotrader.extension.toUnixTimestamp
import com.preslavrachev.cryptotrader.mvc.model.OrderTypeEnum
import com.preslavrachev.cryptotrader.mvc.model.TradeRecord
import com.preslavrachev.cryptotrader.persistence.repository.TradeRecordRepository
import com.preslavrachev.cryptotrader.session.AppSession
import com.preslavrachev.cryptotrader.trading.instrument.timeline.TimelineNode
import com.preslavrachev.cryptotrader.trading.strategy.TradingStrategyDecisionEnum
import com.preslavrachev.cryptotrader.trading.strategy.impl.CandlestickPatternTradingStrategy
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.Scheduled
import remote.poloniex.model.ChartDataEntry
import remote.poloniex.service.PoloniexApiService
import remote.poloniex.service.PoloniexApiService.CurrencyPairEnum.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Logger
import javax.inject.Inject

fun ChartDataEntry.calculateLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
}

@SpringBootApplication
class CryptotraderApplication {

    companion object {
        val LOGGER = Logger.getLogger(CryptotraderApplication::class.simpleName)
    }

    @Inject
    lateinit var session: AppSession

    @Inject
    lateinit var tradeRecordRepository: TradeRecordRepository

    @Inject
    lateinit var poloniexApiService: PoloniexApiService

    @Scheduled(fixedRate = 5 * 60 * 1000)
    fun testStrategy() {
        val end = LocalDateTime.now().toUnixTimestamp()
        val start = end.minusSecondPeriods(5, 300)
        val chartData = poloniexApiService.getChartData(currencyPair = USDT_BTC.label, start = start, end = end)
        val leftSide = chartData.dropLast(1)
        val rightSide = chartData.drop(1)
        val lastPair = leftSide.zip(rightSide)
                .map { (current, prev) ->
                    listOf(
                            TimelineNode(prev.calculateLocalDateTime(), prev.toCandlestick()),
                            TimelineNode(current.calculateLocalDateTime(), current.toCandlestick())
                    )
                }
                .first()

        val strategy = CandlestickPatternTradingStrategy()
        val decision = strategy.decide(lastPair)

        LOGGER.info("Candlestick Pattern Strategy Decision: $decision -- time period $start/$end")

        if (decision == TradingStrategyDecisionEnum.BUY) {
            val trade = TradeRecord(
                    amount = 0.00001f,
                    quoteCurrency = "BTC",
                    baseCurrency = "USDT",
                    baseCurrencyPrice = lastPair[0].content.estimateBuyingPrice(),
                    orderType = OrderTypeEnum.BUY
            )
            tradeRecordRepository.save(trade)
        } else if (decision == TradingStrategyDecisionEnum.SELL) {
            val trade = TradeRecord(
                    amount = 0.00001f,
                    quoteCurrency = "BTC",
                    baseCurrency = "USDT",
                    baseCurrencyPrice = lastPair[0].content.estimateSellingPrice(),
                    orderType = OrderTypeEnum.SELL
            )
            tradeRecordRepository.save(trade)
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CryptotraderApplication::class.java, *args)
}
