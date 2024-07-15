package com.cherry.fm.reservationservices.data.document

import com.cherry.fm.reservationservices.*
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow
import java.sql.Date
import java.time.LocalDate

class DocumentMapper: DbMapper<DocumentEntity> {
    override fun read(row: DbRow): DocumentEntity {
        val id = row.column("id").get() as Long
        val expiration = (row.column("expiration").get() as Date).toLocalDate()
        val number = row.column("number").get() as String
        val type = enumValueOf<DocumentType>(row.column("type").get() as String)
        return DocumentEntity(
            id = id,
            expiration = ExpirationDate(expiration),
            number = number,
            type = type
        )
    }

    override fun toNamedParameters(value: DocumentEntity): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(4)
        map["id"] = value.id
        map["type"] = value.type
        map["expiration"] = value.expiration
        map["number"] = value.number
        return map
    }

    override fun toIndexedParameters(value: DocumentEntity): MutableList<*> {
        val list: MutableList<Any> = ArrayList(4)
        list.add(value.id)
        list.add(value.type)
        list.add(value.expiration)
        list.add(value.number)
        return list
    }
}