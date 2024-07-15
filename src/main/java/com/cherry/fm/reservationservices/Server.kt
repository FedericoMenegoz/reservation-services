package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.error.APIError
import com.cherry.fm.reservationservices.error.BadFormatException
import com.cherry.fm.reservationservices.error.ErrorDTO
import com.cherry.fm.reservationservices.error.NotValidException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpService

class Server(
	controller: HttpService,
	val port: Int = 8080,
) {
	private val server:WebServer by lazy {
		println("Building server...")
		WebServer.builder()
			.mediaContext {
				it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
					registerModule(JavaTimeModule())
					configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				}))
			}
			.routing {
				it.register(controller)
				it.error(BadFormatException::class.java) { _, res, _ ->
					res.status(APIError.BAD_FORMAT.httpStatus)
						.send(ErrorDTO(APIError.BAD_FORMAT))
				}
				it.error(NotValidException::class.java) { _, res, _ ->
					res.status(APIError.VALIDATION_ERROR.httpStatus)
						.send(ErrorDTO(APIError.VALIDATION_ERROR))
				}
			}.port(port).build()
	}

	fun startServer() {
		try	{
			server.start()
			println("Server started, listening on ${server.port()}")
		} catch (e: Exception) {
			println("Server start error: ${e.message}")
		}
	}

	fun stopServer() {
		try {
			server.stop()
			println("Server stopped.")
		} catch (e: Exception) {
			println("Server stopped error: ${e.message}")
		}
	}
}

