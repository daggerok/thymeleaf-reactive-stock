package io.github.daggerok.reactivestock.ticker

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
data class TickerRepository(
    private val tickerIdGenerator: AtomicLong,
    private val tickerStore: ConcurrentHashMap<Long, Ticker>,
) {

    fun save(ticker: Ticker): Mono<Ticker> =
        ticker.copy(id = if (ticker.id == -1L) tickerIdGenerator.getAndIncrement() else ticker.id)
            .also { tickerStore[it.id] = it }
            .also { log.info { "save(ticker=$it)" } }
            .let { Mono.just(it) }

    fun deleteAllInBatch() =
        Mono.fromCallable {
            log.info { "deleteAllInBatch()" }
            tickerStore.clear()
        }

    fun findAll(descending: Boolean = false, predicate: (Ticker) -> Boolean = { true }): Flux<Ticker> =
        tickerStore.values.toFlux().sort(
            Comparator.comparingLong(Ticker::id).let {
                log.info { "findAll(descending=$descending, predicate=$predicate" }
                if (descending) it.reversed() else it
            }
        )

    /*
    fun findPage(pageNumber: Long = 0, pageSize: Long = 10, descending: Boolean = false): Flux<Ticker> =
        Flux.fromIterable(tickerStore.values)
            .sort(
                Comparator.comparing(Ticker::id).let {
                    log.info { "findPage(pageNumber=$pageNumber, pageSize=$pageSize, descending=$descending)" }
                    if (descending) it.reversed() else it
                }
            )
            .skip(pageNumber * pageSize)
            .take(pageSize)
    */

    fun findById(id: Long): Mono<Ticker> =
        Mono.justOrEmpty(
            tickerStore.getOrDefault(id, null)
                .also { log.info { "findById($id): $it" } }
        )

    private companion object {
        val log = logger()
    }
}
