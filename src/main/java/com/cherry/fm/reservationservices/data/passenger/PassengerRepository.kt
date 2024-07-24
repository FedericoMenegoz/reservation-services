package com.cherry.fm.reservationservices.data.passenger

import com.cherry.fm.reservationservices.data.DataRepository
import io.helidon.dbclient.DbClient
import java.util.*

class PassengerRepository(dbClient: DbClient): DataRepository<PassengerEntity>(dbClient) {

    override fun insert(entity: PassengerEntity): Long{
        println("INIT TRANSACTION: $entity")
        val lastId = db.execute().createQuery("INSERT into passenger (birth, first_name, gender, last_name, nationality, type, document_id) values (?, ?, ?, ?, ?, ?, ?) returning id;")
            .params(
                entity.birth.toDate(),
                entity.firstName.toString(),
                entity.gender.toString(),
                entity.lastName.toString(),
                entity.nationality.toString(),
                entity.type.toString(),
                entity.documentId
            ).execute()
            .map { it.column("id").`as`(java.lang.Long::class.java) }
            .findFirst()
            .get()
            .get()

        return lastId.toLong()
    }

    fun getById(id: Long): Optional<PassengerEntity> = db.execute()
        .createQuery("SELECT * FROM passenger WHERE id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(PassengerEntity::class.java)
        }
        .findFirst()
}