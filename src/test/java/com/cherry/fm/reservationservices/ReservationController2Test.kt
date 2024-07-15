package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.services.ReservationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.common.media.type.MediaTypes
import io.helidon.http.Status
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webclient.api.WebClient
import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.Month
import java.util.function.Consumer

class ReservationController2Test {
	/*
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
	*/

	private val reservationService = mock<ReservationService>()

	private val reservation = Reservation(
		passengers = listOf(
			Passenger(
				PassengerType.ADULT,
				Gender.MALE,
				Birth(LocalDate.of(1980, Month.JANUARY, 12)),
				Nationality("ARG"),
				Name("Andres"),
				Name("Sacco"),
				PassengerDocument(
					"20123456783",
					DocumentType.PASSPORT,
					ExpirationDate(LocalDate.of(2027, Month.JULY, 21))
				)
			)
		),
		contact = Contact(
			telephone = ContactNumber("4444-1234"),
			email = ContactEmail("flights@manning.com")
		),
		itineraryId = "f2f61e3c"
	)

	@Test
	fun `check first post response`() {
		val server = Server(ReservationController(reservationService))
		server.startServer()

		whenever(reservationService.saveReservation(reservation)).thenReturn(reservation.copy(id = 1234))

		given()
			.port(8080)
			.`when`()
			.contentType(ContentType.JSON)
			.body(
				"""
				{
				   "passengers":[
				      {
				         "type":"ADULT",
				         "gender":"MALE",
				         "birth":"1980-01-12",
				         "nationality":"ARG",
				         "firstName":"Andres",
				         "lastName":"Sacco",
				         "document":{
				            "number":"20123456783",
				            "type":"PASSPORT",
				            "expiration":"2027-07-21"
				         }
				      }
				   ],
				   "contact":{
				      "telephone":"4444-1234",
				      "email":"flights@manning.com"
				   },
				   "itineraryId":"f2f61e3c"
				}
			""".trimIndent()
			)
			.post("/api/flights/reservation")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(200)
			.body("id", equalTo(1234))

		verify(reservationService).saveReservation(reservation)

		server.stopServer()
	}
}