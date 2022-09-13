package io.github.daggerok.reactivestock.stock

import io.github.daggerok.reactivestock.ticker.Ticker
import io.github.daggerok.reactivestock.ticker.TickerRepository
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.security.SecureRandom
import java.time.Duration
import java.util.function.Consumer
import java.util.stream.Stream

@Configuration
@Profile("!prod", "!production")
class StocksTestDataConfig(
    private val stockSavedPublisher: Consumer<Stock>,
    private val tickerRepository: TickerRepository,
    private val stockRepository: StockRepository,
) {

    @EventListener(ApplicationStartedEvent::class)
    fun generateStockDRN(event: ApplicationStartedEvent) {
        tickerRepository.save(Ticker(name = "DRN"))
            .subscribe()
        val random = SecureRandom()
        // val DRN = tickerRepository.findByName("DRN").orElseThrow()
        val drn = tickerRepository.findAll { it.name == "DRN" }
            .toStream()
            .findFirst()
            .orElseThrow()
        Flux.fromStream(Stream.generate { Stock(ticker = drn) })
            .map { it.copy(price = BigDecimal.valueOf(random.nextDouble(1.00, 5.99)).setScale(2, HALF_UP)) }
            .delayElements(Duration.ofSeconds(random.nextLong(5, 7)))
            .flatMap(stockRepository::save)
            .doOnNext(stockSavedPublisher)
            .subscribe()
    }
}
