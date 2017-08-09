package remote.poloniex.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import remote.poloniex.model.ChartDataEntry
import javax.inject.Inject

@Service
class PoloniexApiService {

    class ChartDataEntryList: ArrayList<ChartDataEntry>()

    enum class CurrencyPairEnum(val label: String) {
        USDT_BTC ("USDT_BTC")
    }

    enum class CommandEnum(val label: String) {
        RETURN_CHART_DATA ("returnChartData")
        ;
    }

    companion object {
        val BASE_URL = "https://poloniex.com/public"
        val COMMAND_PARAM = "command"
        val CURRENCY_PAIR_PARAM = "currencyPair"
        val PERIOD_PARAM = "period"
        val START_PARAM = "start"
        val END_PARAM = "end"
        val URL_BUILDER = DefaultUriBuilderFactory(BASE_URL).builder()
    }

    @Inject
    lateinit var restTemplate: RestTemplate

    fun getChartData(): ChartDataEntryList {
        val url = URL_BUILDER
                .queryParam(COMMAND_PARAM, CommandEnum.RETURN_CHART_DATA.label)
                .queryParam(CURRENCY_PAIR_PARAM, CurrencyPairEnum.USDT_BTC)
                .queryParam(PERIOD_PARAM, 300)
                .queryParam(START_PARAM, 1500000000)
                .queryParam(END_PARAM, 1500030000)
                .build()

        val response = restTemplate.getForEntity(url, ChartDataEntryList::class.java)

        print(response.statusCode)

        return response.body
    }
}