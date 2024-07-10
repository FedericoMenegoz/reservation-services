package com.cherry.fm.reservationservices

import io.helidon.http.Status

class ReservationService(
	private val map:MutableMap<Int, Reservation> = mutableMapOf(),
	private var counter: Int = 0
) {

	fun saveReservation(reservation: Reservation): ReservationResponse {
		println("Saving reservation...")
		map[counter] = reservation
		return ReservationResponse(counter++, reservation.passengers, reservation.contact, reservation.itineraryId)
	}

	fun getReservationById(id: Int): ReservationResponse {
		println("Getting reservation number $id...")
		return ReservationResponse(id, map[id]!!.passengers, map[id]!!.contact, map[id]!!.itineraryId)
	}
}