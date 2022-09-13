package io.github.daggerok.reactivestock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveStockApplication

fun main(args: Array<String>) {
    runApplication<ReactiveStockApplication>(*args)
}
