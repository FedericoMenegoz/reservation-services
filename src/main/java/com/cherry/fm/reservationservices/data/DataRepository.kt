package com.cherry.fm.reservationservices.data

import io.helidon.dbclient.DbClient

abstract class DataRepository<T> (protected val db: DbClient ){


	abstract fun insert(entity: T): Long

}