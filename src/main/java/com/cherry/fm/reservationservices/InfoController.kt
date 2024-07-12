package com.cherry.fm.reservationservices

import io.helidon.config.Config
import io.helidon.config.MissingValueException
import io.helidon.http.Status
import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.http.ServerRequest
import io.helidon.webserver.http.ServerResponse

class InfoController : Controller {
	private val config: Config = Config.create()

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

	override fun initEndpoints(builder: HttpRouting.Builder) {
		builder.get("/info/{attr}", { req, res -> getInfo(req, res)})
	}
}