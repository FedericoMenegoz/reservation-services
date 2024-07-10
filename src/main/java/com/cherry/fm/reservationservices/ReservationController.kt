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
) {
	private val config: Config = Config.create()

	private val server by lazy {
		WebServer.builder()
			.config(config.get("server"))
			.mediaContext {
				it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
					registerModule(JavaTimeModule())
					configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				}))
			}
			.routing {
				it.post("/api/flights/reservation",  { req, res -> makeReservation(req, res)})
				it.get("/api/flights/reservation/{id}", { req, res -> getReservation(req, res)})
			}.build()
	}

	private fun getReservation(req: ServerRequest, res: ServerResponse) {
		val id = req
			.path()
			.pathParameters()
			.get("id")
			.toString()
			.toInt()

		res.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(resService.getReservationById(id))
	}

	private fun makeReservation(req: ServerRequest, res: ServerResponse) {
		val request = req.content().`as`(Reservation::class.java)
		if (request.itineraryId == null) {
			res.status(Status.BAD_REQUEST_400).send()
			return
		}

		res
			.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(resService.saveReservation(request))
	}

	fun startServer() {
		server.start()
	}

	fun stopServer() {
		server.stop()
	}
}


data class Reservation(
	var passengers: List<Passenger>,
	var contact: Contact,
	var itineraryId: String? = null,
) {
	constructor() : this(listOf(), Contact("", ""), null)
}

@JvmRecord
data class ReservationResponse(
	val id: Int,
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String? = null,
)

