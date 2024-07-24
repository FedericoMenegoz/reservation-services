package com.cherry.fm.reservationservices.data.reservation_passengers

import com.cherry.fm.reservationservices.data.DataRepository
import io.helidon.dbclient.DbClient

class ReservationPassengersRepository(dbClient: DbClient): DataRepository<ReservationPassengersEntity>(dbClient) {
    override fun insert(entity: ReservationPassengersEntity): Long {
        val lastId = db.execute().createQuery(
            "INSERT into reservation_passengers (reservation_id, passengers_id) values (?, ?) returning reservation_id;"
        ).params(
            entity.reservationId,
            entity.passengersId,
        ).execute()
        .map { it.column("reservation_id").`as`(java.lang.Long::class.java) }
            .findFirst()
            .get()
            .get()

        return lastId.toLong()
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
