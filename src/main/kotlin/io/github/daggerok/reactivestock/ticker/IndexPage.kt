package io.github.daggerok.reactivestock.ticker

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering

@Controller
class IndexPage(private val tickerRepository: TickerRepository) {

    @GetMapping("/", "/ticker", "/tickers")
    fun indexHtml() =
        Rendering.view("index")
            .also { log.info { "indexHtml()" } }
            .modelAttribute("tickers", tickerRepository.findAll())
            .build()

    private companion object {
        val log = logger()
    }
}
