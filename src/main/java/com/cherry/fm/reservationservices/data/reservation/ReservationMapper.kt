package com.cherry.fm.reservationservices.data.reservation

import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow

class ReservationMapper: DbMapper<ReservationEntity> {
    override fun read(row: DbRow): ReservationEntity {
        val id = row.column("id").get() as Long
        val itinerary_id = row.column("itinerary_id").get() as String
        val contact_id = row.column("contact_id").get() as Long
        return ReservationEntity(
            id = id,
            itineraryId = itinerary_id,
            contactId = contact_id
        )
    }

    override fun toNamedParameters(value: ReservationEntity): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(3)
        map["id"] = value.id
        map["itinerary_id"] = value.itineraryId
        map["contact_id"] = value.contactId
        return map
    }

    override fun toIndexedParameters(value: ReservationEntity): MutableList<*> {
        val list: MutableList<Any> = ArrayList(3)
        list.add(value.id)
        list.add(value.itineraryId)
        list.add(value.contactId)
        return list
    }
}