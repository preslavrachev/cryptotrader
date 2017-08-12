package com.preslavrachev.cryptotrader.extension

import org.apache.commons.codec.binary.Hex
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

fun String.encrypt512(key: String): String {
    val METHOD = "HmacSHA512"
    val mac = Mac.getInstance(METHOD)
    mac.init(SecretKeySpec(key.toByteArray(), METHOD))
    return String(Hex.encodeHex(mac.doFinal(this.toByteArray())))
}
