package io.github.daggerok.reactivestock.stock

import io.github.daggerok.reactivestock.ticker.Ticker
import java.math.BigDecimal
import java.time.Instant

data class Stock(
    val ticker: Ticker,
    val price: BigDecimal = BigDecimal.ZERO,
    val timestamp: Instant = Instant.now(),
    val id: Long = -1,
)
