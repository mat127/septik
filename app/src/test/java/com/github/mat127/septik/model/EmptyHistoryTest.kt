package com.github.mat127.septik.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class EmptyHistoryTest {

    val history = EmptyHistory()

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
    fun whenOnlyEmptyRecordExists_thenReturned() {
        val ts = Instant.now()
        history.add(ts)
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

    @Test
    fun whenMoreEmptyRecordsExist_thenLastIsReturned() {
        val ts = Instant.now().minus(1, ChronoUnit.DAYS)
        history.add(ts.minus(2, ChronoUnit.HOURS))
        history.add(ts)
        history.add(ts.minus(1, ChronoUnit.HOURS))
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

}