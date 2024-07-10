package com.cherry.fm.reservationservices

import java.time.LocalDate

@JvmRecord
data class PassengerDocument(
	val number: String,
	val type: DocumentType,
	val expiration: LocalDate,
)