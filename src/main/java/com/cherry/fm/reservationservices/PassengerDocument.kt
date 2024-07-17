package com.cherry.fm.reservationservices

import java.time.LocalDate

@JvmRecord
data class PassengerDocument(
	val number: String,
	val type: DocumentType,
	val expiration: ExpirationDate,
)

@JvmInline
value class ExpirationDate (val date: LocalDate) {
	init {
		require(date.isAfter(LocalDate.now())) {
			"Date is expired."
		}
	}

	override fun toString(): String = date.toString()
	fun toDate() = date
}
