package com.cherry.fm.reservationservices.data.reservation

import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.reservation_passengers.ReservationPassengersEntity
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import io.helidon.dbclient.DbStatementException
import java.math.BigInteger
import java.util.*

class ReservationRepository: DataRepository<ReservationEntity>() {
    override fun insert(entity: ReservationEntity): Long {
        val transaction = db.transaction()
        try {
            transaction.createInsert(
                "INSERT into reservation (itinerary_id, contact_id) values (?, ?)"
            ).params(
                entity.itineraryId,
                entity.contactId.toString()
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

    fun getById(id: Long): Optional<ReservationEntity> = db.execute()
        .createQuery("SELECT * FROM reservation WHERE id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(ReservationEntity::class.java)
        }
        .findFirst()

}