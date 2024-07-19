package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.services.ReservationService
import io.helidon.common.media.type.MediaTypes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class ValidationTest {
	private val reservationService: ReservationService = mock()

	@Test
	fun `value classes parsing with invalid firstname test`() {
		val port = (1024..49151).random()
		val server: Server = Server(port, ReservationController(reservationService))
		server.startServer()

		given()
			.port(port)
			.`when`()
			.contentType(ContentType.JSON)
			.body("""
					{
						"firstName":"A",
						"lastName":"Sacco",
						"gender":"MALE",
						"nationality":"ARG",
						"type":"ADULT",
						"birth":"1980-01-12",
						"documentExpiration":"2027-07-21",
						"documentNumber":"20123456783",
						"documentType":"PASSPORT",
						"contactTelephone":"4444-1234",
						"contactEmail":"flights@manning.com",
						"itineraryId":"f2f61e3c"
					}
				""".trimIndent()
			)
			.post("/api/flights/reservation")
			.then()
			.statusCode(400)


		verify(reservationService, never()).saveReservation(any())
	}

	@Test
	fun `value classes parsing with invalid lastname test`() {
		val port = (1024..49151).random()
		val server: Server = Server(controller = arrayOf(ReservationController(reservationService)), port = port)
		server.startServer()

		given()
			.port(port)
			.`when`()
			.contentType(ContentType.JSON)
			.body("""
					{
						"firstName":"Andres",
						"lastName":"r",
						"gender":"MALE",
						"nationality":"ARG",
						"type":"ADULT",
						"birth":"1980-01-12",
						"documentExpiration":"2027-07-21",
						"documentNumber":"20123456783",
						"documentType":"PASSPORT",
						"contactTelephone":"4444-1234",
						"contactEmail":"flights@manning.com",
						"itineraryId":"f2f61e3c"
					}
				""".trimIndent()
			)
			.post("/api/flights/reservation")
			.then()
			.statusCode(400)


		verify(reservationService, never()).saveReservation(any())
	}

	@Test
	fun `bad format test`() {
		val port = (1024..49151).random()
		val server: Server = Server(port = port, controller = arrayOf(ReservationController(reservationService)))
		server.startServer()

		given()
			.port(port)
			.`when`()
			.contentType(ContentType.JSON)
			.body("""{
					}
				""".trimIndent()
			)
			.post("/api/flights/reservation")
			.then()
			.statusCode(400)


		verify(reservationService, never()).saveReservation(any())
	}
}