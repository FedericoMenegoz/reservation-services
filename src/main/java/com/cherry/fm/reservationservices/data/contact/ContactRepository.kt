package com.cherry.fm.reservationservices.data.contact

import com.cherry.fm.reservationservices.data.DataRepository
import io.helidon.dbclient.DbClient
import io.helidon.dbclient.DbStatementException
import java.lang.Exception
import java.lang.reflect.Executable
import java.math.BigInteger
import java.util.*

class ContactRepository (dbClient: DbClient): DataRepository<ContactEntity>(dbClient) {
	override fun insert(entity: ContactEntity): Long {
		val lastId = db.execute().createQuery(
			"INSERT INTO contact (email, telephone) VALUES (?, ?) RETURNING id;"
		).params(
			entity.email.toString(),
			entity.telephone.toString()
		).execute()
			.map { it.column("id").`as`(java.lang.Long::class.java) }
			.findFirst()
			.get()
			.get()


		return lastId.toLong()
	}

	fun getById(id: Long): Optional<ContactEntity> = db.execute()
		.createQuery("SELECT * FROM contact WHERE id = ?")
		.params(id)
		.execute()
		.map {
			it.`as`(ContactEntity::class.java)
		}
		.findFirst()
}
