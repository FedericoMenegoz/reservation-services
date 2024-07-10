package com.cherry.fm.reservationservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.config.Config
import io.helidon.http.Status
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.ServerRequest
import io.helidon.webserver.http.ServerResponse


class ReservationController(
	private val resService: ReservationService = ReservationService(),
	val port: Int = 8080,
) {
	private val config: Config = Config.create()

	private val server by lazy {
		println("Building server...")
		WebServer.builder()
//			.config(config.get("server"))
			.mediaContext {
				it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
					registerModule(JavaTimeModule())
					configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				}))
			}
			.routing {
				it.post("/api/flights/reservation",  { req, res -> makeReservation(req, res)})
				it.get("/api/flights/reservation/{id}", { req, res -> getReservation(req, res)})
			}.port(port).build()
	}

	private fun getReservation(req: ServerRequest, res: ServerResponse) {
		val id = req
			.path()
			.pathParameters()
			.get("id")
			.toString()
			.toInt()
		println("GET request with id: $id")
		res.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(resService.getReservationById(id))
		println("GET response sent.")
	}

	private fun makeReservation(req: ServerRequest, res: ServerResponse) {
		val request = req.content().`as`(Reservation::class.java)
		if (request.itineraryId == null) {
			res.status(Status.BAD_REQUEST_400).send()
			println("POST error itineraryId is undefined...")
			return
		}
		println("POST received: $request")
		res
			.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(resService.saveReservation(request))
		println("POST response sent...")
	}

	fun startServer() {
		try	{
			server.start()
			println("Server started, listening on ${server.port()}")
		} catch (e: Exception) {
			println("Server start error: ${e.message}")
		}
	}

	fun stopServer() {
		try {
			server.stop()
			println("Server stopped.")
		} catch (e: Exception) {
			println("Server stopped error: ${e.message}")
		}
	}
}


data class Reservation(
	var passengers: List<Passenger> = listOf(),
	var contact: Contact = Contact("", ""),
	var itineraryId: String? = null,
)

@JvmRecord
data class ReservationResponse(
	val id: Int,
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String? = null,
)

