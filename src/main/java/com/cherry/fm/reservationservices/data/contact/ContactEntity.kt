package com.cherry.fm.reservationservices.data.contact

import com.cherry.fm.reservationservices.ContactEmail
import com.cherry.fm.reservationservices.ContactNumber

data class ContactEntity (
	val id: Long,
	val telephone: ContactNumber,
	val email: ContactEmail,
)