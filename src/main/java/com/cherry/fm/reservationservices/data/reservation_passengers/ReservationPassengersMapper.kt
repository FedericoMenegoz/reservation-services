package com.cherry.fm.reservationservices.data.reservation_passengers

import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow

class ReservationPassengersMapper: DbMapper<ReservationPassengersEntity> {
    override fun read(row: DbRow): ReservationPassengersEntity {
        val reservation_id = row.column("reservation_id").get() as Long
        val passengers_id = row.column("passengers_id").get() as Long
        return ReservationPassengersEntity(
            reservationId = reservation_id,
            passengersId = passengers_id
        )
    }

    override fun toNamedParameters(value: ReservationPassengersEntity): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(2)
        map["reservation_id"] = value.reservationId
        map["passengers_id"] = value.passengersId
        return map
    }

    override fun toIndexedParameters(value: ReservationPassengersEntity): MutableList<*> {
        val list: MutableList<Any> = ArrayList(2)
        list.add(value.reservationId)
        list.add(value.passengersId)
        return list
    }
}