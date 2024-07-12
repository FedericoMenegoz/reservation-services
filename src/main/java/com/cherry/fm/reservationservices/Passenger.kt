package com.cherry.fm.reservationservices

import java.time.LocalDate

@JvmRecord
data class Passenger(
	val type: PassengerType,
	val gender: Gender,
	val birth: Birth,
	val nationality: Nationality,
	val firstName: Name,
	val lastName: Name,
	val document: PassengerDocument
)

@JvmInline
value class Name(private val name: String) {
	init {
		require(
			name.isNotBlank() && name.length >= 2 && name.length <= 20
		) {
			"A name must be of length from 2 to 20."
		}
	}

	override fun toString(): String = name
}

@JvmInline
value class Nationality(private val nationality: String) {
	init {
		require(nationality.isNotBlank() && nationality.length == 3 && nationality.uppercase() == nationality) {
			"nationality must be a 3-UpperCase-Char string"
		}
	}

	override fun toString(): String = nationality
}

@JvmInline
value class Birth(private val date: LocalDate) {
	init {
		require(date.isAfter(LocalDate.now().minusYears(120) )){
			"Invalid birthdate: ${date.dayOfMonth}.${date.monthValue}.${date.year}"
		}
	}

	override fun toString(): String = date.toString()
}
