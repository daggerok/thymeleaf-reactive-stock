package io.github.daggerok.reactivestock.ticker

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Configuration
class TickerDatabaseConfig {

    @Bean
    fun tickerStore(): ConcurrentHashMap<Long, Ticker> =
        ConcurrentHashMap()

    @Bean
    fun tickerIdGenerator(): AtomicLong =
        AtomicLong(0)
}
