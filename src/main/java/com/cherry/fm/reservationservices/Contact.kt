package com.cherry.fm.reservationservices

import com.fasterxml.jackson.annotation.JsonProperty

@JvmRecord
data class Contact (
	val telephone: String,
	val email: String,
)