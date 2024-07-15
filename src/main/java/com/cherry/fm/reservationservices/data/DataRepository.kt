package com.cherry.fm.reservationservices.data

import com.cherry.fm.reservationservices.data.passenger.PassengerEntity
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import java.util.*
import kotlin.reflect.KClass

abstract class DataRepository<T> {
	private val conf: Config = Config.create()
	protected val db: DbClient = DbClient.create(conf["db"])

	abstract fun insert(entity: T): Long
	//abstract fun getById(id: Long): Optional<T>
}