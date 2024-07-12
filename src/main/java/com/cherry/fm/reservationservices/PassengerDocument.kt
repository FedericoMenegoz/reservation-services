package com.cherry.fm.reservationservices

import java.time.LocalDate

@JvmRecord
data class PassengerDocument(
	val number: DocumentNumber,
	val type: DocumentType,
	val expiration: ExpirationDate,
)

@JvmInline
value class ExpirationDate (val date: LocalDate) {
	init {
		println(date)
		require(date.isAfter(LocalDate.now())) {
			"Date is expired."
		}
	}

	override fun toString(): String = date.toString()
}

@JvmInline
value class DocumentNumber(private val number: String) {
	init {
		require(number.length == 11 && number.all { it.isDigit() }) {
			"Invalid document number: $number"
		}
	}

	override fun toString(): String = number
}