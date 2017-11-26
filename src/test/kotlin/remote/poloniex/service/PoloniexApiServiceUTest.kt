package remote.poloniex.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.preslavrachev.cryptotrader.extension.encrypt512
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.http.ResponseEntity
import org.springframework.util.ResourceUtils
import org.springframework.web.client.RestTemplate
import java.net.URI

class PoloniexApiServiceUTest {

    companion object {
        val API_SECRET = "abcd"
    }

    val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    @Mock
    lateinit var restTemplate: RestTemplate

    @InjectMocks
    lateinit var apiService: PoloniexApiService

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        apiService.apply {
            apiSecret = API_SECRET
        }
    }

    @Test
    fun testChartDataIntegrity(): Unit {
        val file = ResourceUtils.getFile("classpath:snapshots/poloniex/chartdata.json")
        val chartDataResult = objectMapper.readValue<PoloniexApiService.ChartDataEntryList>(file)

        `when`(restTemplate.getForEntity(
                ArgumentMatchers.any(URI::class.java),
                ArgumentMatchers.eq(PoloniexApiService.ChartDataEntryList::class.java)
        )).thenReturn(ResponseEntity.ok(chartDataResult))

        val chartData = apiService.getChartData(1L, 1L)

        assertThat(chartData.first(), equalTo(chartData.minBy { it.date }))
        assertThat(chartData.last(), equalTo(chartData.maxBy { it.date }))

        val averageHigh = chartData.map { it.high }.average()
        val averageLow = chartData.map { it.low }.average()
        println("Average high: $averageHigh | Average low: $averageLow")
        assertTrue(averageHigh >= averageLow)
    }

    @Test
    fun testSignatureCreation() {
        val params = mapOf(Pair("foo", "bar"), Pair("bar", "foo"))

        val expectedSignature = "foo=bar&bar=foo".encrypt512(API_SECRET)
        assertThat(apiService.createSignature(params), equalTo(expectedSignature))
    }
}
