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
	private val map:MutableMap<Int, Reservation> = mutableMapOf(),
	private var counter: Int = 0
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
/*	object LocalDB {
		private var counter = 0
		fun add(reservation: Reservation): ReservationResponse {
			val resp = ReservationResponse(counter, reservation.passengers, reservation.contact, reservation.itineraryId)
			map[counter++] = reservation
			return resp
		}
		fun get(id: Int) = map[id]

	}*/
	private fun getReservation(req: ServerRequest, res: ServerResponse) {
		val id = req
			.path()
			.pathParameters()
			.get("id")
			.toString()
			.toInt()

		res.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(ReservationResponse(id, map[id]!!.passengers, map[id]!!.contact, map[id]!!.itineraryId))
	}

	private fun makeReservation(req: ServerRequest, res: ServerResponse) {
		val request = req.content().`as`(Reservation::class.java)
		map[counter] = request
		res
			.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(ReservationResponse(counter++, request.passengers, request.contact, request.itineraryId))
	}

	fun startServer() {
		server.start()
	}

	fun stopServer() {
		server.stop()
	}
}

@JvmRecord
data class Reservation(
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String,
)

@JvmRecord
data class ReservationResponse(
	val id: Int,
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String,
)

