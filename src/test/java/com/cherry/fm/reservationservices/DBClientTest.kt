package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.data.contact.ContactRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.common.media.type.MediaTypes
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webclient.api.WebClient
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.function.Consumer

class DBClientTest {

    private val reservation = Reservation(
        passengers =
        listOf(Passenger(
            PassengerType.ADULT,
            Gender.MALE,
            Birth(LocalDate.of(1980, Month.JANUARY, 12)),
            Nationality("ARG"),
            Name("Andres"),
            Name("Sacco"),
            PassengerDocument(
                "20123456783",
                DocumentType.PASSPORT,
                ExpirationDate(LocalDate.of(2027, Month.JULY, 21 ))
            )
        )),
        contact = Contact(
            telephone = ContactNumber("4444-1234"),
            email = ContactEmail("flights@manning.com")
        ),
        itineraryId = "f2f61e3c"
    )

    private fun webClient(): WebClient? = WebClient.builder()
        .mediaContext(Consumer {
            it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
                registerModule(JavaTimeModule())
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            }))
        })
        .build()

    private fun sentPostCheckResponse(
        client: WebClient,
        reservation: Reservation
    ) = client.post()
        .uri("http://localhost:8080")
        .contentType(MediaTypes.APPLICATION_JSON)
        .path("/api/flights/reservation")
        .submit(reservation)

    @Test
    fun dbTest(){
        val x = ContactRepository()
        println(x.getById(2))


    }

    @Test
    fun `test insert contact`(){
        val server = Server(controller = arrayOf(ReservationController()))
        server.startServer()
        val client = webClient()!!
        val r = sentPostCheckResponse(client, reservation)
            .inputStream()
            .bufferedReader()
            .readText()

    }
}