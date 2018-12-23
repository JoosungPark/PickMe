package com.joosung.pickme.util

import android.os.ConditionVariable
import com.joosung.pickme.common.RealmRepository
import io.realm.Realm

typealias RealmQueryRunner = ((Realm) -> Unit)

class RealmTransactionRunner {
    val queries = ArrayList<RealmQueryRunner>()

    fun add(runner: RealmQueryRunner): RealmTransactionRunner {
        queries.add(runner)
        return this
    }

    fun commit(repo: RealmRepository) {
        if (queries.count() > 0) {
            val cvTrans = ConditionVariable()
            repo.queryRealm { realm ->
                val db = realm.realm
                db.beginTransaction()
                try {
                    queries.forEach { it(db) }
                    db.commitTransaction()
                } catch (ex: Exception) {
                    db.cancelTransaction()
                    ex.printStackTrace()
                    throw ex
                } finally {
                    cvTrans.open()
                }
            }.subscribe()
            cvTrans.block()
        }
    }
}
