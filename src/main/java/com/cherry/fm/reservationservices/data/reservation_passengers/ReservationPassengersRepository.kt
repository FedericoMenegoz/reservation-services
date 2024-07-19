package com.cherry.fm.reservationservices.data.reservation_passengers

import com.cherry.fm.reservationservices.data.DataRepository
import com.cherry.fm.reservationservices.data.document.DocumentEntity
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import java.math.BigInteger
import java.util.*

class ReservationPassengersRepository(dbClient: DbClient): DataRepository<ReservationPassengersEntity>(dbClient) {
    override fun insert(entity: ReservationPassengersEntity): Long {
        val transaction = db.transaction()
        transaction.createInsert(
            "INSERT into reservation_passengers (reservation_id, passengers_id) values (?, ?)"
        ).params(
            entity.reservationId.toString(),
            entity.passengersId.toString(),
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

    fun getByReservationId(id: Long): List<ReservationPassengersEntity> = db.execute()
        .createQuery("SELECT * FROM reservation_passengers WHERE reservation_id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(ReservationPassengersEntity::class.java)
        }.toList()

    fun getByPassengerId(id: Long): List<ReservationPassengersEntity> = db.execute()
        .createQuery("SELECT * FROM reservation_passengers WHERE passengers_id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(ReservationPassengersEntity::class.java)
        }.toList()
}
