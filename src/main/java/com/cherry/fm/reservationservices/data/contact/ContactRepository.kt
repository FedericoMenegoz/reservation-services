package com.cherry.fm.reservationservices.data.contact

import com.cherry.fm.reservationservices.data.DataRepository
import io.helidon.dbclient.DbClient
import io.helidon.dbclient.DbStatementException
import java.math.BigInteger
import java.util.*

class ContactRepository (dbClient: DbClient): DataRepository<ContactEntity>(dbClient) {

	override fun insert(entity: ContactEntity): Long {
		val transaction = db.transaction()
		try {
			transaction.createInsert(
				"INSERT into contact (email, telephone) values (?, ?)"
			).params(
				entity.email.toString(),
				entity.telephone.toString()
			).execute()
			val lastID = transaction.createQuery("SELECT LAST_INSERT_ID() as lastId")
				.execute()
				.map { it.column("lastId").`as`(BigInteger::class.java) }
				.findFirst()
				.get()
				.get()
			transaction.commit()

			return lastID.toLong()
		} catch (e: DbStatementException) {
			transaction.rollback()
			throw e
		}
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
