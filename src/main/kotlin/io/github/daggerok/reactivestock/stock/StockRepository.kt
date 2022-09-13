package io.github.daggerok.reactivestock.stock

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
data class StockRepository(
    private val stockIdGenerator: AtomicLong,
    private val stockStore: ConcurrentHashMap<Long, Stock>,
) {

    fun save(stock: Stock): Mono<Stock> =
        stock.copy(id = if (stock.id == -1L) stockIdGenerator.getAndIncrement() else stock.id)
            .also { stockStore[it.id] = it }
            .also { log.info { "save($it)" } }
            .let { Mono.just(it) }

    /*
    fun save(vararg stocks: Stock): Flux<Stock> =
        stocks.toFlux().flatMap(this::save)

    fun deleteAllInBatch() =
        Mono.fromCallable {
            log.info { "deleteAllInBatch()" }
            stockStore.clear()
        }
    */

    fun findAll(descending: Boolean = false, predicate: (Stock) -> Boolean = { true }): Flux<Stock> =
        stockStore.values.toFlux().filter(predicate).or(Flux.empty()).sort(
            Comparator.comparingLong(Stock::id).let {
                log.info { "findAll(descending=$descending, predicate=$predicate" }
                if (descending) it.reversed() else it
            }
        )

    private companion object {
        val log = logger()
    }
}
