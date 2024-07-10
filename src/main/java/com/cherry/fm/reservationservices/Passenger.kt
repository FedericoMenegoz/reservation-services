package com.cherry.fm.reservationservices

import java.time.LocalDate

@JvmRecord
data class Passenger (
	val type: PassengerType,
	val gender: Gender,
	val birth: LocalDate,
	val nationality: String,
	val firstName: String,
	val lastName: String,
	val document: PassengerDocument
)

