package com.cherry.fm.reservationservices.data.passenger

import com.cherry.fm.reservationservices.Contact
import com.cherry.fm.reservationservices.Passenger
import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.reservation.ReservationEntity
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import java.math.BigInteger
import java.util.*

class PassengerRepository: DataRepository<PassengerEntity>() {

    override fun insert(entity: PassengerEntity): Long{
        println("INIT TRANSACTION: $entity")
        val transaction = db.transaction()
        transaction.createInsert("INSERT into passenger (birth, first_name, gender, last_name, nationality, type, document_id) values (?, ?, ?, ?, ?, ?, ?)")
            .params(
                entity.birth.toString(),
                entity.firstName.toString(),
                entity.gender.toString(),
                entity.lastName.toString(),
                entity.nationality.toString(),
                entity.type.toString(),
                entity.documentId
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

    fun getById(id: Long): Optional<PassengerEntity> = db.execute()
        .createQuery("SELECT * FROM passenger WHERE id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(PassengerEntity::class.java)
        }
        .findFirst()
}