package com.cherry.fm.reservationservices.data

import com.cherry.fm.reservationservices.Contact
import com.cherry.fm.reservationservices.ContactEmail
import com.cherry.fm.reservationservices.ContactNumber
import io.helidon.dbclient.DbColumn
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.DbRow


class ContactMapper: DbMapper<Contact> {
    override fun read(row: DbRow): Contact {
        println("INIZIO MAPPER")
        val id = row.column("id").get() as Long
        val email = row.column("email").get() as String
        val telephone = row.column("telephone").get() as String
        println("MAPPER: $id $telephone $email")
        return Contact(telephone =  ContactNumber(telephone), email =  ContactEmail(email))
    }

    override fun toNamedParameters(value: Contact): MutableMap<String, *> {
        val map: MutableMap<String, Any> = HashMap(2)
        map["email"] = value.email
        map["telephone"] = value.telephone
        return map
    }

    override fun toIndexedParameters(value: Contact): MutableList<*> {
        val list: MutableList<Any> = ArrayList(2)
        list.add(value.email)
        list.add(value.telephone)
        return list
    }
}