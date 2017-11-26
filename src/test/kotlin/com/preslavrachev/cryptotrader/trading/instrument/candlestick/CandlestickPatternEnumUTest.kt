package com.preslavrachev.cryptotrader.trading.instrument.candlestick

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.springframework.util.ResourceUtils

class CandlestickPatternEnumUTest {

    val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    lateinit var candlestickData: HashMap<CandlestickPatternEnum, List<Pair<Candlestick,Candlestick>>>

    @Before
    fun setUp() {
        val file = ResourceUtils.getFile("classpath:candlesticks/candlesticks.json")
        candlestickData = objectMapper.readValue(file)
    }

    @Test
    fun testPatternIdentification() {
        candlestickData.forEach { entry ->
            Assert.assertThat(entry.value.all {
                val patternsMatch = entry.key.evaluator(it.first, it.second)
                if(!patternsMatch) {
                    println("Candlestick pair ${entry.value} does not match pattern ${entry.key}")
                }
                patternsMatch
            }, equalTo(true))
        }
    }

}