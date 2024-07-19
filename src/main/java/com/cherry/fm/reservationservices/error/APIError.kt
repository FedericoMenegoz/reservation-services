package com.cherry.fm.reservationservices.error

import io.helidon.http.Status

enum class APIError ( val httpStatus: Status, val message: String) {
	VALIDATION_ERROR(Status.BAD_REQUEST_400, "Validation error"),
	BAD_FORMAT(Status.BAD_REQUEST_400, "JSON format error"),
	NOT_FOUND(Status.NOT_FOUND_404, "Reservation not found")
}

