package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.services.ReservationService
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.Month

class ReservationControllerTest {
	private val reservation = Reservation(
		passengers =
		listOf(
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

	private val reservationService = mock<ReservationService>()

	@Test
	fun `check first post response`() {
		val port = (1024..49151).random()

		val server = Server(controller = arrayOf(ReservationController(reservationService)), port = port)
		server.startServer()

		whenever(reservationService.saveReservation(reservation)).thenReturn(reservation.copy(id = 1234))

		given()
			.port(port)
			.`when`()
			.contentType(ContentType.JSON)
			.body(
				"""
			{
				"firstName":"Andres",
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
			.contentType(ContentType.JSON)
			.statusCode(200)
			.body("id", equalTo(1234))

		verify(reservationService).saveReservation(reservation)

		server.stopServer()
	}


	@Test
	fun `check get after post`() {

		val port = (1024..49151).random()
		val server = Server(port = port, controller = arrayOf(ReservationController(reservationService)))

		whenever(reservationService.getReservationById(1)).thenReturn(reservation.copy(id = 1))

		server.startServer()
		given()
			.port(port)
			.`when`()
			.get("/api/flights/reservation/1")
			.then()
			.statusCode(200)
			.body("passengers[0].firstName", equalTo("Andres"))
			.body("contact.telephone", equalTo("4444-1234"))
			.body("itineraryId", equalTo("f2f61e3c"))

		server.stopServer()
	}

	@Test
	fun `post without itineraryId should return BAD_REQUEST_400`() {
		val port = (1024..49151).random()

		val server = Server(controller = arrayOf(ReservationController(reservationService)), port = port)
		server.startServer()


		given()
			.port(port)
			.`when`()
			.contentType(ContentType.JSON)
			.body(
				"""
			{
				"firstName":"Andres",
				"lastName":"Sacco",
				"gender":"MALE",
				"nationality":"ARG",
				"type":"ADULT",
				"birth":"1980-01-12",
				"documentExpiration":"2027-07-21",
				"documentNumber":"20123456783",
				"documentType":"PASSPORT",
				"contactTelephone":"4444-1234",
				"contactEmail":"flights@manning.com"
			}
			""".trimIndent()
			)
			.post("/api/flights/reservation")
			.then()
			.statusCode(400)
		verify(reservationService, never()).saveReservation(any())
		server.stopServer()
	}
}