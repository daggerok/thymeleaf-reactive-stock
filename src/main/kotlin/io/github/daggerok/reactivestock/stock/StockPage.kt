package io.github.daggerok.reactivestock.stock

import org.apache.logging.log4j.kotlin.logger
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux

@Controller
class StockPage(private val stockRepository: StockRepository, private val stockSubscription: Flux<Stock>) {

    @GetMapping("/stock/{ticker}")
    fun stockHtml(@PathVariable ticker: String) =
        Rendering.view("stock")
            .also { log.info { "stockHtml(ticker=$ticker)" } }
            .modelAttribute("ticker", ticker)
            .modelAttribute(
                "stocks",
                ReactiveDataDriverContextVariable(
                    stockRepository
                        .findAll(descending = true) { it.ticker.name == ticker }
                        .take(1),
                    1
                )
            )
            .build()

    @GetMapping("/stock/{ticker}/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stockPartialHtml(@PathVariable ticker: String) =
        Rendering.view("stock :: #stocks")
            .also { log.info { "stockPartialHtml(ticker=$ticker)" } }
            .modelAttribute("ticker", ticker)
            .modelAttribute("stocks", ReactiveDataDriverContextVariable(stockSubscription, 1))
            .build()

    private companion object {
        val log = logger()
    }
}
