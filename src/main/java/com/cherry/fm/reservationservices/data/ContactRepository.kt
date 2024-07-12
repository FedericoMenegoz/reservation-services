package com.cherry.fm.reservationservices.data

import com.cherry.fm.reservationservices.Contact
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import java.util.stream.Collector

class ContactRepository {
    private val conf: Config = Config.create()
    private val db: DbClient = DbClient.create(conf["db"])

    fun getById(id: Int) = db
        .execute()
        .createQuery("SELECT * FROM contact WHERE id = ?")
        .params(id)
        .execute()
        .map {
            it.`as`(Contact::class.java)
        }.toList()
}