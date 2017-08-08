package com.preslavrachev.cryptotrader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CryptotraderApplication

fun main(args: Array<String>) {
    SpringApplication.run(CryptotraderApplication::class.java, *args)
}
