package com.preslavrachev.cryptotrader.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class MainAppConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(KotlinModule())
    }

    @Bean
    @Primary
    fun mappingJacksonHttpMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter(objectMapper)
    }

    @Bean
    fun restTemplate(mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter): RestTemplate {
        return RestTemplate().apply {
            messageConverters.add(0, mappingJackson2HttpMessageConverter)
        }
    }
}
