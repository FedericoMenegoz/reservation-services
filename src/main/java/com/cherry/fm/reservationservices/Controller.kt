package com.cherry.fm.reservationservices

import io.helidon.webserver.http.HttpRouting

interface Controller {
	fun initEndpoints(builder: HttpRouting.Builder): Unit
}