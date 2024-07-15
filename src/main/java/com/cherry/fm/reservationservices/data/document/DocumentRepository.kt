package com.cherry.fm.reservationservices.data.document

import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.passenger.PassengerEntity
import java.math.BigInteger
import java.util.*

class DocumentRepository: DataRepository<DocumentEntity>() {
    override fun insert(entity: DocumentEntity): Long {
        println("INIT TRANSACTION: $entity")
        val transaction = db.transaction()
        transaction.createInsert(
            "INSERT into document (expiration, number, type) values (?, ?, ?)"
        ).params(
            entity.expiration.toString(),
            entity.number,
            entity.type.toString(),
        ).execute()
        val lastID = transaction.createQuery("SELECT LAST_INSERT_ID() as lastId")
            .execute()
            .map { it.column("lastId").`as`(BigInteger::class.java) }
            .findFirst()
            .get()
            .get()

        transaction.commit()

        return lastID.toLong()
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