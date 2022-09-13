package io.github.daggerok.reactivestock.stock

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Configuration
class StockDatabaseConfig {

    @Bean
    fun stockStore(): ConcurrentHashMap<Long, Stock> =
        ConcurrentHashMap()

    @Bean
    fun stockIdGenerator(): AtomicLong =
        AtomicLong(0)
}
