package com.cherry.fm.reservationservices.data.passenger

import com.cherry.fm.reservationservices.Contact
import io.helidon.common.Weight
import io.helidon.dbclient.DbMapper
import io.helidon.dbclient.spi.DbMapperProvider
import java.util.*

/**
 * Provides pokemon mappers.
 */
@Weight(100.0)
class PassengerMapperProvider : DbMapperProvider {
    override fun <T> mapper(type: Class<T>): Optional<DbMapper<T>> {
        if (type == PassengerEntity::class.java) {
            
            return Optional.of(MAPPER as DbMapper<T>)
        }
        return Optional.empty()
    }

    companion object {
        private val MAPPER: PassengerMapper = PassengerMapper()
    }
}