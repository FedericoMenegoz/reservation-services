package com.cherry.fm.reservationservices

class ErrorDTO (
	val code: Int,
	val shortDescription: String,
	val reason: String
) {
	constructor(apiError: APIError) : this(apiError.httpStatus.code(), apiError.message, apiError.httpStatus.text())
}


