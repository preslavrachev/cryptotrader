package com.preslavrachev.cryptotrader.extension

import org.junit.Assert
import org.junit.Test

internal class CryptoUTest {

    @Test
    fun testSha512Encryption() {
        val encryptedString = "test".encrypt512("foobar")
        Assert.assertEquals(encryptedString, "e6cabfe668785dded3a5c17ddd33ba9db220fbccbb2c10f0184cf50acab3838d86f5cf8d2fab44029516b1eec6d31c82da3af9b7cadccea756e15b3c6445350d")
    }

}
