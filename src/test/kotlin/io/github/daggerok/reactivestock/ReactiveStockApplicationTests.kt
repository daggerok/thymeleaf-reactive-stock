package io.github.daggerok.reactivestock

import io.github.daggerok.reactivestock.ticker.TickerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.test.test

@SpringBootTest
class ReactiveStockApplicationTests(@Autowired val ticKerRepository: TickerRepository) {

    @BeforeEach
    fun `before each`() {
        ticKerRepository.deleteAllInBatch().subscribe()
    }

    @Test
    fun `should not find by id not existed`() {
        // when
        ticKerRepository.findById(123L)
            // then
            .test()
            .expectNextCount(0)
            .verifyComplete()
    }

    /*
    @Test
    fun `should find page`() {
        // given
        Flux.range(0, 15)
            .map { Ticker(name = "ticker $it") }
            .flatMap(ticKerRepository::save)
            .test()
            .expectNextCount(15)
            .verifyComplete()

        // when
        ticKerRepository.findPage()
            .doOnNext { log.info { "ascending: $it" } }
            // then
            .test()
            .expectNextCount(10)
            .verifyComplete()

        // and when
        ticKerRepository.findPage(descending = true)
            .doOnNext { log.info { "descending: $it" } }
            // then
            .test()
            .expectNextCount(10)
            .verifyComplete()

        // and when
        ticKerRepository.findPage(pageNumber = 2, pageSize = 3)
            .doOnNext { log.info { "Page(number=2, pageSize=3, ascending): $it" } }
            // then
            .test()
            .expectNextCount(3)
            .verifyComplete()

        // and when
        ticKerRepository.findPage(1, 5, true)
            .doOnNext { log.info { "Page(number=1, pageSize=5, descending): $it" } }
            // then
            .test()
            .expectNextCount(5)
            .verifyComplete()
    }

    companion object {
        val log = logger()
    }
    */
}
