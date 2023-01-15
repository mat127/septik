package com.github.mat127.septik.model

import java.time.Instant

interface State {
    val timestamp: Instant
    val state: Double
}