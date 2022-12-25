package com.github.mat127.septik.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class StateHistoryTest {

    val history = StateHistory()

    @Before
    fun clearHistory() {
        history.clear()
    }

    @Test
    fun whenEmpty() {
        assertThat(history.getLastEmptyTimestamp())
            .isNull()
    }

    @Test
    fun whenNotEmptyButWih() {
        history.add(LocalDateTime.now(), 2.34)
        assertThat(history.getLastEmptyTimestamp())
            .isNull()
    }

    @Test
    fun whenOnlyEmptyRecordExists_thenReturned() {
        val ts = LocalDateTime.now()
        history.addEmpty(ts)
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

    @Test
    fun whenEmptyRecordExists_thenReturned() {
        val ts = LocalDateTime.now().minus(1, ChronoUnit.DAYS)
        history.add(ts.minus(1, ChronoUnit.HOURS), 2.34)
        history.add(ts.plus(1, ChronoUnit.HOURS), 2.34)
        history.addEmpty(ts)
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

    @Test
    fun whenMoreEmptyRecordsExist_thenLastIsReturned() {
        val ts = LocalDateTime.now().minus(1, ChronoUnit.DAYS)
        history.add(ts.plus(1, ChronoUnit.HOURS), 2.34)
        history.add(ts.plus(2, ChronoUnit.HOURS), 2.34)
        history.addEmpty(ts)
        history.addEmpty(ts.minus(1, ChronoUnit.HOURS))
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

    @Test
    fun whenEmpty_thenNoSpeed() {
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isNull()
    }

    @Test
    fun whenOnlyEmpty_thenNoSpeed() {
        history.addEmpty(LocalDateTime.now())
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isNull()
    }

    @Test
    fun whenOnly1State_thenNoSpeed() {
        history.add(LocalDateTime.now(), 2.34)
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isNull()
    }

    @Test
    fun when2States_thenSpeedIsReturned() {
        val now = LocalDateTime.now()
        val states = arrayOf(
            Pair(now.minusDays(5), 1.23),
            Pair(now.minusDays(1), 2.34)
        )
        states.forEach { history.add(it.first, it.second) }
        history.addEmpty(now.minusDays(3))
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isEqualTo((states[1].second - states[0].second)/Duration.between(states[0].first,states[1].first).seconds)
    }


    @Test
    fun whenMoreThan2States_thenCalculationIntervalIsUsed() {
        val now = LocalDateTime.now()
        val states = arrayOf(
            Pair(now.minusDays(7), 0.12),
            Pair(now.minusDays(3), 1.23),
            Pair(now.minusDays(1), 2.34)
        )
        states.forEach { history.add(it.first, it.second) }
        history.addEmpty(now.minusDays(5))
        assertThat(history.getSpeed(Duration.ofDays(5)))
            .isEqualTo((states[2].second - states[0].second)/Duration.between(states[0].first,states[2].first).seconds)
    }
}
