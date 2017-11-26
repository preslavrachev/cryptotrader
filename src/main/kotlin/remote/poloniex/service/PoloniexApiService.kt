package remote.poloniex.service

import com.preslavrachev.cryptotrader.extension.encrypt512
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import remote.poloniex.model.ChartDataEntry
import javax.inject.Inject

@Service
class PoloniexApiService {

    @Value("\${api.poloniex.key}")
    lateinit var apiKey: String

    @Value("\${api.poloniex.secret}")
    lateinit var apiSecret: String

    class ChartDataEntryList: ArrayList<ChartDataEntry>()

    enum class CurrencyPairEnum(val label: String) {
        USDT_BTC  ("USDT_BTC"),
        BTC_ETH   ("BTC_ETH"),
        BTC_XRP   ("BTC_XRP"),
        BTC_DASH  ("BTC_DASH"),
    }

    enum class CommandEnum(val label: String) {
        RETURN_CHART_DATA ("returnChartData"),
        RETURN_BALANCES   ("returnBalances")
        ;
    }

    companion object {
        val BASE_URL = "https://poloniex.com"
        val PUBLIC_URL = BASE_URL + "/public"
        val TRADE_URL = BASE_URL + "/tradingApi"
        val COMMAND_PARAM = "command"
        val CURRENCY_PAIR_PARAM = "currencyPair"
        val PERIOD_PARAM = "period"
        val START_PARAM = "start"
        val END_PARAM = "end"
        val PUBLIC_URL_BUILDER = DefaultUriBuilderFactory(PUBLIC_URL).builder()
        val TRADE_URL_BUILDER = DefaultUriBuilderFactory(TRADE_URL).builder()
    }

    @Inject
    lateinit var restTemplate: RestTemplate

    fun getChartData(start: Long, end: Long): ChartDataEntryList {
        val url = PUBLIC_URL_BUILDER
                .queryParam(COMMAND_PARAM, CommandEnum.RETURN_CHART_DATA.label)
                .queryParam(CURRENCY_PAIR_PARAM, CurrencyPairEnum.USDT_BTC)
                .queryParam(PERIOD_PARAM, 300)
                .queryParam(START_PARAM, start)
                .queryParam(END_PARAM, end)
                .build()

        val response = restTemplate.getForEntity(url, ChartDataEntryList::class.java)

        print(response.statusCode)

        return response.body
    }


    @Suppress("UNCHECKED_CAST")
    fun returnBalances(): Map<String, Float> {
        val url = TRADE_URL_BUILDER.build()

        val bodyParams = LinkedMultiValueMap<String, String>().apply {
            add(COMMAND_PARAM, CommandEnum.RETURN_BALANCES.label)
            add("nonce", System.currentTimeMillis().toString())
        }

        val headers = HttpHeaders().apply {
            add("Key", apiKey)
            add("Sign", createSignature(bodyParams.toSingleValueMap()))
            add("Content-Type", "application/x-www-form-urlencoded")
        }

        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(bodyParams, headers)

        return restTemplate.postForEntity(url, request, Map::class.java).body as Map<String, Float>
    }

    internal fun createSignature(params: Map<String, String>): String {
        return params.entries.asSequence()
                .joinToString(separator = "&") { entry -> "${entry.key}=${entry.value}" }
                .encrypt512(apiSecret)
    }
}
