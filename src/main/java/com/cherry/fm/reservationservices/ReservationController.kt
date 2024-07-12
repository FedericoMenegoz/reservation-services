package com.cherry.fm.reservationservices

import io.helidon.http.Status
import io.helidon.webserver.http.HttpRules
import io.helidon.webserver.http.HttpService
import io.helidon.webserver.http.ServerRequest
import io.helidon.webserver.http.ServerResponse


class ReservationController(
	private val resService: ReservationService = ReservationService(),
) : HttpService {

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


	override fun routing(rules: HttpRules) {
		rules.post("/api/flights/reservation", this::makeReservation)
		rules.get("/api/flights/reservation/{id}", this::getReservation)

	}
}

@JvmRecord
data class Reservation(
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String?,
)

@JvmRecord
data class ReservationResponse(
	val id: Int,
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String? = null,
)

