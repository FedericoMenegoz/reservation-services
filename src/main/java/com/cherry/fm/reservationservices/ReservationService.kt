package com.cherry.fm.reservationservices

import io.helidon.http.Status

class ReservationService(
	private val map:MutableMap<Int, Reservation> = mutableMapOf(),
	private var counter: Int = 0
) {

	fun saveReservation(reservation: Reservation): Reservation {
		println("Saving reservation...")
		map[counter] = reservation
		return Reservation(counter++, reservation.passengers, reservation.contact, reservation.itineraryId)
	}

	fun getReservationById(id: Int): Reservation {
		println("Getting reservation number $id...")
		return Reservation(id, map[id]!!.passengers, map[id]!!.contact, map[id]!!.itineraryId)
	}
}