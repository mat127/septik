package com.github.mat127.septik.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class StateHistoryTest {

    val history = StateHistory()

    @Before
    fun clearHistory() {
        history.clear()
    }

    @Test
    fun whenEmpty_thenNoSpeed() {
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isNull()
    }

    @Test
    fun whenOnly1State_thenNoSpeed() {
        history.add(Instant.now(), 2.34)
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isNull()
    }

    @Test
    fun when2States_thenSpeedIsReturned() {
        val now = Instant.now()
        val states = arrayOf(
            Pair(now.minus(5, ChronoUnit.DAYS), 1.23),
            Pair(now.minus(1, ChronoUnit.DAYS), 2.34)
        )
        states.forEach { history.add(it.first, it.second) }
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isEqualTo((states[1].second - states[0].second)/Duration.between(states[0].first,states[1].first).seconds)
    }


    @Test
    fun whenMoreThan2States_thenCalculationIntervalIsUsed() {
        val now = Instant.now()
        val states = arrayOf(
            Pair(now.minus(7, ChronoUnit.DAYS), 0.12),
            Pair(now.minus(3, ChronoUnit.DAYS), 1.23),
            Pair(now.minus(1, ChronoUnit.DAYS), 2.34)
        )
        states.forEach { history.add(it.first, it.second) }
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isEqualTo((states[2].second - states[0].second)/Duration.between(states[0].first,states[2].first).seconds)
    }
}
