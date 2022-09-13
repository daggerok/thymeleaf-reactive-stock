package io.github.daggerok.reactivestock.stock

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.util.function.Consumer

@Configuration
@Profile("!prod", "!production")
class StockProcessingConfig(
    private val stockRepository: StockRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    @Value("\${app.buffer-size:2048}") val bufferSize: Int,
) {

    @Bean
    fun stockProcessor(): Many<Stock> =
        Sinks.many()
            .multicast()
            .directBestEffort<Stock>() // .onBackpressureBuffer(bufferSize);

    @Bean
    fun stockSavedPublisher(stockProcessor: Many<Stock>): Consumer<Stock> =
        Consumer<Stock> { stockProcessor.tryEmitNext(it) }

    @Bean
    fun stockStreamScheduler(): Scheduler =
        Schedulers.newSingle("stockStreamScheduler")

    @Bean
    fun stockSubscription(stockStreamScheduler: Scheduler, stockProcessor: Many<Stock>): Flux<Stock> =
        stockProcessor.asFlux()
            .publishOn(stockStreamScheduler)
            .subscribeOn(stockStreamScheduler)
            .onBackpressureBuffer(bufferSize) // tune me if you wish...
    // .share() // DO NOT SHARE when using newer reactor API, such as Sinks.many()...!
}
