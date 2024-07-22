package com.cherry.fm.reservationservices.data.reservation

import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.reservation_passengers.ReservationPassengersEntity
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import io.helidon.dbclient.DbStatementException
import java.math.BigInteger
import java.util.*

class ReservationRepository(dbClient: DbClient): DataRepository<ReservationEntity>(dbClient) {
    override fun insert(entity: ReservationEntity): Long {
        val lastId = db.execute().createQuery(
            "INSERT into reservation (itinerary_id, contact_id) values (?, ?) returning id;"
        ).params(
            entity.itineraryId,
            entity.contactId
        ).execute()
        .map { it.column("id").`as`(java.lang.Long::class.java) }
            .findFirst()
            .get()
            .get()

        return lastId.toLong()
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