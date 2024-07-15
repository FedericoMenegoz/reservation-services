package com.cherry.fm.reservationservices.data.passenger

import com.cherry.fm.reservationservices.*

data class PassengerEntity(
	val id: Long,
	val type: PassengerType,
	val gender: Gender,
	val birth: Birth,
	val nationality: Nationality,
	val firstName: Name,
	val lastName: Name,
	val documentId: Long
)
