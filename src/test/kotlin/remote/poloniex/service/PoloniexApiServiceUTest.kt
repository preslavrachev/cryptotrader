package remote.poloniex.service

import com.preslavrachev.cryptotrader.extension.encrypt512
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PoloniexApiServiceUTest {

    companion object {
        val API_SECRET = "abcd"
    }

    lateinit var apiService: PoloniexApiService

    @Before
    fun setUp() {
        apiService = PoloniexApiService().apply {
            apiSecret = API_SECRET
        }
    }

    @Test
    fun testSignatureCreation() {
        val params = mapOf(Pair("foo", "bar"), Pair("bar", "foo"))

        val expectedSignature = "foo=bar&bar=foo".encrypt512(API_SECRET)
        assertEquals(apiService.createSignature(params), expectedSignature)
    }
}
