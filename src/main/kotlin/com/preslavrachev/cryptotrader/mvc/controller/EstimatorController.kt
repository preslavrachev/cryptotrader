package com.preslavrachev.cryptotrader.mvc.controller

import com.preslavrachev.cryptotrader.mvc.logic.SupportResistanceEstimationService
import com.preslavrachev.cryptotrader.mvc.logic.SupportResistanceResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@Controller
@RequestMapping("/estimator")
class EstimatorController {

    @Inject
    private lateinit var supportResistanceEstimationService: SupportResistanceEstimationService

    @GetMapping("/suppress/{pair}")
    @ResponseBody
    fun getSupportResistanceForTradingPair(@PathVariable(name = "pair") tradingPair: String, @RequestParam(name = "points", defaultValue = "1000") numberOfDataPoints: Long): SupportResistanceResponse {
        return supportResistanceEstimationService.estimateSupportResistanceForTradingPair(tradingPair, numberOfDataPoints)
    }
}
