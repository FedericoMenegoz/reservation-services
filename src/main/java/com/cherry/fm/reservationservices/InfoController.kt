package com.cherry.fm.reservationservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.config.Config
import io.helidon.config.MissingValueException
import io.helidon.http.Status
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.ServerRequest
import io.helidon.webserver.http.ServerResponse

class InfoController (
val port: Int = 8080,
) {
	private val config: Config = Config.create()

	private val server by lazy {
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
				it.get("/info/{attr}", { req, res -> getInfo(req, res)})
			}.port(port).build()
	}

	private fun getInfo(req: ServerRequest, res: ServerResponse) {
		val attr = req
			.path()
			.pathParameters()
			.get("attr")
			.toString()
		println("GET request with id: $attr")

		try {
			val resText = config["app.$attr"].asString().string
			if (resText != null) {

				res.header("Content-Type", "application/json")
					.status(Status.OK_200)
					.send(resText)
				println("GET response sent.")
			}
		} catch (e: MissingValueException) {
			res.status(Status.NOT_FOUND_404).send("$attr not found.")
		}

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