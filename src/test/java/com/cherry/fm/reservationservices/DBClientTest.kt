package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.data.ContactRepository
import org.junit.jupiter.api.Test

class DBClientTest {
    @Test
    fun dbTest(){
        val x = ContactRepository()
        println(x.getById(2))
    }
}