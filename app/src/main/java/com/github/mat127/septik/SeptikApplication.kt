package com.github.mat127.septik

import android.app.Application
import com.github.mat127.septik.db.SeptikDatabase
import com.github.mat127.septik.model.EmptyHistory
import com.github.mat127.septik.model.Septik
import com.github.mat127.septik.model.StateHistory

class SeptikApplication: Application() {

    val database by lazy { SeptikDatabase.getDatabase(this.applicationContext) }

    val septik by lazy {
        val emptyHistory = EmptyHistory(database.emptyDao())
        val stateHistory = StateHistory(database.stateDao())
        Septik(stateHistory, emptyHistory)
    }
}