package com.cherry.fm.reservationservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.config.Config
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpService

class Server(
	controller: HttpService,
	val port: Int = 8080,
) {
	private val config: Config = Config.create()

	val server by lazy {
		println("Building server...")
		WebServer.builder()
//			.config(config.get("server"))
			.mediaContext {
				it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
					registerModule(JavaTimeModule())
					configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				}))
			}
			.routing {
				it.register(controller)
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

