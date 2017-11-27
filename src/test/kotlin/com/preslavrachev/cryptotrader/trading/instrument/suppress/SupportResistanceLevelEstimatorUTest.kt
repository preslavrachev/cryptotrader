package com.preslavrachev.cryptotrader.trading.instrument.suppress

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.preslavrachev.cryptotrader.trading.helper.PriceAndTimestamp
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.springframework.util.ResourceUtils

class SupportResistanceLevelEstimatorUTest {

    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    
    @Test
    fun estimateSupportResistanceLevel() {
        val file = ResourceUtils.getFile("classpath:snapshots/generic/prices.json")
        val input = objectMapper.readValue<List<PriceAndTimestamp>>(file)

        val estimator = SupportResistanceLevelEstimator()
        val support = estimator.estimateSupportResistanceLevel(input)

        assertThat(support, `is`(8188.2188159839525f))
    }

}
