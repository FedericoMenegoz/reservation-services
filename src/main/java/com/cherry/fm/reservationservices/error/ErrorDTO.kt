package com.cherry.fm.reservationservices.error

class ErrorDTO (
	val code: Int,
	val shortDescription: String,
	val reason: String
) {
	constructor(apiError: APIError) : this(apiError.httpStatus.code(), apiError.message, apiError.httpStatus.text())
}


