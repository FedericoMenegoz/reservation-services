package com.cherry.fm.reservationservices.data.contact

import com.cherry.fm.reservationservices.ContactEmail
import com.cherry.fm.reservationservices.ContactNumber
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow

class ContactMapper: DbMapper<ContactEntity> {
    override fun read(row: DbRow): ContactEntity {
        val id = row.column("id").get() as Long
        val email = row.column("email").get() as String
        val telephone = row.column("telephone").get() as String
        return ContactEntity(id = id, telephone = ContactNumber(telephone), email = ContactEmail(email))
    }

    override fun toNamedParameters(value: ContactEntity): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(3)
        map["id"] = value.id
        map["email"] = value.email
        map["telephone"] = value.telephone
        return map
    }

    override fun toIndexedParameters(value: ContactEntity): MutableList<*> {
        val list: MutableList<Any> = ArrayList(3)
        list.add(value.id)
        list.add(value.email)
        list.add(value.telephone)
        return list
    }
}