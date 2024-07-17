package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.services.ReservationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.common.media.type.MediaTypes
import io.helidon.http.Status
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webclient.api.WebClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.function.Consumer

class ReservationControllerTest {
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
	fun `check first post response`() {
		val server = Server(controller = arrayOf(ReservationController(ReservationService())))
		server.startServer()
		val client = webClient()!!
		val r  = sentPostCheckResponse(client, reservation)
			.inputStream()
			.bufferedReader()
			.readText()


		assertEquals( """
				{
				   "id":13,
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
			""".trimIndent().filter { it.toString().isNotBlank() }, r)

		server.stopServer()
	}




	@Test
	fun `check get after post` () {
		val server = Server(controller = arrayOf(ReservationController(ReservationService())))
		server.startServer()
		val client = webClient()!!
		val response = client.get().uri("http://localhost:8080").path("/api/flights/reservation/1").request()

		assertEquals(Status.OK_200, response.status())
		assertEquals("""
				{
				   "id":1,
				   "passengers":[
				      {
				         "type":"ADULT",
				         "gender":"FEMALE",
				         "birth":"1980-01-12",
				         "nationality":"ARG",
				         "firstName":"Andres",
				         "lastName":"Sacco",
				         "document":{
				            "number":"3",
				            "type":"PASSPORT",
				            "expiration":"2025-02-28"
				         }
				      }
				   ],
				   "contact":{
				      "telephone":"4444-2345",
				      "email":"flights@manning.com"
				   },
				   "itineraryId":"4"
				}
			""".trimIndent().filter { it.toString().isNotBlank() },
			response
				.inputStream()
				.bufferedReader()
				.readText())

		server.stopServer()
	}

	@Test
	fun `post without itineraryId should return BAD_REQUEST_400`() {
		val server = Server(controller = arrayOf(ReservationController()))
		server.startServer()

		val client = webClient()!!

		val res2 = Reservation(
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
			itineraryId = null
		)

		assertEquals(Status.BAD_REQUEST_400, sentPostCheckResponse(client, res2).status())

		server.stopServer()
	}
}