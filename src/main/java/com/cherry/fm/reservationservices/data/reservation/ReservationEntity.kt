package com.cherry.fm.reservationservices.data.reservation

data class ReservationEntity(
	val id: Long,
	val itineraryId: String,
	val contactId: Long
)
