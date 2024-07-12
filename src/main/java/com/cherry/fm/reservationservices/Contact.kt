package com.cherry.fm.reservationservices

@JvmRecord
data class Contact (
	val id: Long? = null,
	val telephone: ContactNumber,
	val email: ContactEmail,
)

@JvmInline
value class ContactNumber(private val number: String) {
	init {
		require(number.matches(Regex("^[0-9]{4}-[0-9]{4}$"))) {
			"Invalid telephone number"
		}
	}

	override fun toString(): String = number
}

@JvmInline
value class ContactEmail(private val email: String) {
	init {
		require(email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"))) {
			"Invalid email"
		}
	}

	override fun toString(): String = email
}