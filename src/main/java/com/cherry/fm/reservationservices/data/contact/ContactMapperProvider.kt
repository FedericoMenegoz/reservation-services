package com.cherry.fm.reservationservices.data.contact

import com.cherry.fm.reservationservices.Contact
import io.helidon.common.Weight
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.spi.DbMapperProvider
import java.util.*

/**
 * Provides pokemon mappers.
 */
@Weight(100.0)
class ContactMapperProvider : DbMapperProvider {
    override fun <T> mapper(type: Class<T>): Optional<DbMapper<T>> {
        if (type == ContactEntity::class.java) {
            
            return Optional.of(MAPPER as DbMapper<T>)
        }
        return Optional.empty()
    }

    companion object {
        private val MAPPER: ContactMapper = ContactMapper()
    }
}