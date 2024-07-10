package com.cherry.fm.reservationservices

import io.helidon.http.Status

class ReservationService(
	private val map:MutableMap<Int, Reservation> = mutableMapOf(),
	private var counter: Int = 0
) {

	fun saveReservation(reservation: Reservation): ReservationResponse {
		map[counter] = reservation
		return ReservationResponse(counter++, reservation.passengers, reservation.contact, reservation.itineraryId)
	}

	fun getReservationById(id: Int): ReservationResponse {
		return ReservationResponse(id, map[id]!!.passengers, map[id]!!.contact, map[id]!!.itineraryId)
	}
}