package com.cherry.fm.reservationservices.data.document

import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.passenger.PassengerEntity
import io.helidon.dbclient.DbClient
import java.math.BigInteger
import java.util.*

class DocumentRepository (dbClient: DbClient): DataRepository<DocumentEntity>(dbClient) {
    override fun insert(entity: DocumentEntity): Long {
        println("INIT TRANSACTION: $entity")
        val lastId = db.execute().createQuery(
            "INSERT into document (expiration, number, type) values (?, ?, ?) returning id;"
        ).params(
            entity.expiration.toDate(),
            entity.number,
            entity.type.toString(),
        ).execute()
        .map { it.column("id").`as`(java.lang.Long::class.java) }
            .findFirst()
            .get()
            .get()

        return lastId.toLong()
    }

    fun getById(id: Long): Optional<DocumentEntity> = db.execute()
        .createQuery("SELECT * FROM document WHERE id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(DocumentEntity::class.java)
        }
        .findFirst()

}