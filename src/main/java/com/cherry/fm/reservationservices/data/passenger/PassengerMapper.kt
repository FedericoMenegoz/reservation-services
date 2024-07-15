package com.cherry.fm.reservationservices.data.passenger

import com.cherry.fm.reservationservices.*
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow
import java.sql.Date
import java.time.LocalDate

class PassengerMapper: DbMapper<PassengerEntity> {
    override fun read(row: DbRow): PassengerEntity {
        val id = row.column("id").get() as Long
        val birth = (row.column("birth").get() as Date).toLocalDate()
        val first_name = Name(row.column("first_name").get() as String)
        val last_name = Name(row.column("last_name").get() as String)
        val gender = enumValueOf<Gender>(row.column("gender").get() as String)
        val nationality = Nationality(row.column("nationality").get() as String)
        val type = enumValueOf<PassengerType>(row.column("type").get() as String)
        val document_id = row.column("document_id").get() as Long

        return PassengerEntity(
            id = id,
            birth = Birth(birth),
            firstName = first_name,
            lastName = last_name,
            gender = gender,
            nationality = nationality,
            type = type,
            documentId = document_id
        )
    }

    override fun toNamedParameters(value: PassengerEntity): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(8)
        map["id"] = value.id
        map["birth"] = value.birth
        map["first_name"] = value.firstName
        map["last_name"] = value.lastName
        map["gender"] = value.gender
        map["nationality"] = value.nationality
        map["type"] = value.type
        map["document_id"] = value.documentId
        return map
    }

    override fun toIndexedParameters(value: PassengerEntity): MutableList<*> {
        val list: MutableList<Any> = ArrayList(8)
        list.add(value.id)
        list.add(value.birth)
        list.add(value.firstName)
        list.add(value.lastName)
        list.add(value.gender)
        list.add(value.nationality)
        list.add(value.type)
        list.add(value.documentId)
        return list
    }
}