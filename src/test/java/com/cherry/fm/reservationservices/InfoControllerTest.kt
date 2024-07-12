package com.cherry.fm.reservationservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webclient.api.WebClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.function.Consumer

class InfoControllerTest {
	private fun webClient(): WebClient? = WebClient.builder()
		.mediaContext(Consumer {
			it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
				registerModule(JavaTimeModule())
				configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			}))
		})
		.build()
	@Test
	fun `testing basic info mock`() {
		val server = Server(controller = InfoController())
		server.startServer()
		val client = webClient()!!
		assertEquals("com.cherry.fm.reservationservices", client.get().uri("http://localhost:8080/").path("info/groupId").request().inputStream().bufferedReader().readText())
		assertEquals("cherry-manning", client.get().uri("http://localhost:8080/").path("info/artifactId").request().inputStream().bufferedReader().readText())
		assertEquals("0.1", client.get().uri("http://localhost:8080/").path("info/version").request().inputStream().bufferedReader().readText())
		assertEquals("ciao not found.", client.get().uri("http://localhost:8080/").path("info/ciao").request().inputStream().bufferedReader().readText())
		server.stopServer()
	}
}