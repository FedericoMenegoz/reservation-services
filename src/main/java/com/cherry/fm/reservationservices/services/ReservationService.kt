package com.cherry.fm.reservationservices.services

import com.cherry.fm.reservationservices.*
import com.cherry.fm.reservationservices.data.contact.ContactEntity
import com.cherry.fm.reservationservices.data.contact.ContactRepository
import com.cherry.fm.reservationservices.data.document.DocumentEntity
import com.cherry.fm.reservationservices.data.document.DocumentRepository
import com.cherry.fm.reservationservices.data.passenger.PassengerEntity
import com.cherry.fm.reservationservices.data.passenger.PassengerRepository
import com.cherry.fm.reservationservices.data.reservation.ReservationEntity
import com.cherry.fm.reservationservices.data.reservation.ReservationRepository
import com.cherry.fm.reservationservices.data.reservation_passengers.ReservationPassengersEntity
import com.cherry.fm.reservationservices.data.reservation_passengers.ReservationPassengersRepository
import io.helidon.dbclient.DbStatementException

class ReservationService {
	private val repoPassenger = PassengerRepository()
	private val repoDocument = DocumentRepository()
	private val repoContact = ContactRepository()
	private val repoReservation = ReservationRepository()
	private val repoReservationPassenger = ReservationPassengersRepository()

	fun saveReservation(reservation: Reservation): Reservation {
		println("Saving reservation...")
		var contactId: Long? = null
		var reservationId: Long? = null
		val documentIds = mutableListOf<Long>()
		val passengerIds = mutableListOf<Long>()
		val (_, passengers, contact, itineraryId) = reservation
		try {
			contactId = repoContact.insert(ContactEntity(-1, contact.telephone, contact.email))
			reservationId = repoReservation.insert(ReservationEntity(
				id = -1,
				itineraryId = itineraryId!!,
				contactId = contactId))
			println("PASSENGERS: $passengers")
			passengers.forEach() { passenger ->
				println("before")

				documentIds.add(repoDocument.insert(DocumentEntity(
					id = -1,
					expiration = passenger.document.expiration,
					number =  passenger.document.number,
					type =  passenger.document.type
				)))

				println("DOCUMENTS: $documentIds")

				val passengerId = repoPassenger.insert(PassengerEntity(
					id = -1,
					birth = passenger.birth,
					firstName = passenger.firstName,
					gender = passenger.gender,
					lastName = passenger.lastName,
					nationality = passenger.nationality,
					type = passenger.type,
					documentId = documentIds.last()))
				repoReservationPassenger.insert(ReservationPassengersEntity(
					passengersId = passengerId,
					reservationId = reservationId
				))

			}
			return reservation.copy(id= reservationId)
		} catch (e: DbStatementException) {
			println(e)
			throw e
		}
	}

	fun getReservationById(id: Long): Reservation {
		println("Getting reservation number $id...")
		val reservation = repoReservation.getById(id).get()
		val passengers = mutableListOf<Passenger>()

		repoReservationPassenger.getByReservationId(id).forEach{
			val currPassEnt = repoPassenger.getById(it.passengersId).get()
			val docEnt = repoDocument.getById(currPassEnt.documentId).get()
			val doc = PassengerDocument(
				number = docEnt.number,
				type = docEnt.type,
				expiration = docEnt.expiration,
			)
			val pass = Passenger(
				firstName = currPassEnt.firstName,
				lastName = currPassEnt.lastName,
				birth = currPassEnt.birth,
				nationality = currPassEnt.nationality,
				gender = currPassEnt.gender,
				type = currPassEnt.type,
				document = doc
			)

			passengers.add(pass)
		}
		val contactEnt = repoContact.getById(reservation.contactId).get()

		return Reservation(
			id = id,
			passengers = passengers,
			contact = Contact(
				telephone = contactEnt.telephone,
				email = contactEnt.email
			),
			itineraryId = reservation.itineraryId,
		)
	}
}