package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.error.BadFormatException
import com.cherry.fm.reservationservices.error.NotValidException
import com.cherry.fm.reservationservices.services.ReservationService
import io.helidon.http.Status
import io.helidon.http.media.jackson.JacksonRuntimeException
import io.helidon.webserver.http.HttpRules
import io.helidon.webserver.http.HttpService
import io.helidon.webserver.http.ServerRequest
import io.helidon.webserver.http.ServerResponse
import java.util.StringJoiner


class ReservationController(
	private val resService: ReservationService = ReservationService(),
) : HttpService {

	private fun getReservation(req: ServerRequest, res: ServerResponse) {
		val id = req
			.path()
			.pathParameters()
			.get("id")
			.toString()
			.toLong()

		println("GET request with id: $id")
		res.header("Content-Type", "application/json")
			.status(Status.OK_200)
			.send(resService.getReservationById(id))
		println("GET response sent.")
	}

	private fun makeReservation(req: ServerRequest, res: ServerResponse) {
		val unparsed = req.content()
		//println(unparsed.inputStream().bufferedReader().readText())
		var request: Reservation
		try {
			val requ = unparsed.`as`(ReservationRequest::class.java)
			request = Reservation(
				id = null,
				passengers = listOf(
					Passenger(
						firstName = requ.firstName,
						lastName = requ.lastName,
						birth = Birth(requ.birth.toDate()),
						type = requ.type,
						gender = requ.gender,
						nationality = requ.nationality,
						document = PassengerDocument(
							expiration = ExpirationDate(requ.documentExpiration.toDate()),
							type = requ.documentType,
							number = requ.documentNumber
						)
					)
				),
				contact = Contact(
					telephone = requ.contactTelephone,
					email = requ.contactEmail
				),
				itineraryId = requ.itineraryId
			)
		} catch (e: JacksonRuntimeException) {
			throw BadFormatException()
		} catch (e: IllegalArgumentException) {
			throw NotValidException()
		}

		try {
			request = request.copy(passengers = request.passengers.map {
				it.copy(firstName = Name(it.firstName.toString()))
			})
		}
		catch (e: IllegalArgumentException) {
			throw NotValidException()
		}
		try {
			request = request.copy(passengers = request.passengers.map {
				it.copy(lastName = Name(it.lastName.toString()))
			})
		}
		catch (e: IllegalArgumentException) {
			throw NotValidException()
		}
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
	val id: Long? = null,
	val passengers: List<Passenger>,
	val contact: Contact,
	val itineraryId: String?,
)


@JvmRecord
data class ReservationRequest(
	val firstName: Name,
	val lastName: Name,
	val type: PassengerType,
	val gender: Gender,
	val birth: Birth,
	val nationality: Nationality,
	val documentExpiration: ExpirationDate,
	val documentNumber: String,
	val documentType: DocumentType,
	val contactTelephone: ContactNumber,
	val contactEmail: ContactEmail,
	val itineraryId: String?
)