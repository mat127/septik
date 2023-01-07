package com.github.mat127.septik.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
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
        val ts = LocalDateTime.now()
        history.add(ts)
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

    @Test
    fun whenMoreEmptyRecordsExist_thenLastIsReturned() {
        val ts = LocalDateTime.now().minus(1, ChronoUnit.DAYS)
        history.add(ts.minusHours(2))
        history.add(ts)
        history.add(ts.minusHours(1))
        assertThat(history.getLastEmptyTimestamp())
            .isEqualTo(ts)
    }

}